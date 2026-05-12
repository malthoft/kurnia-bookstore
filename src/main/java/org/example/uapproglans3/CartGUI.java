package org.example.uapproglans3;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

/**
 * CartGUI - GUI keranjang belanja.
 *
 * === PENERAPAN PRINSIP SOLID ===
 *
 * SRP: completeTransaction() dipecah menjadi method kecil:
 *   validateAllStock(), createTransaction(), saveTransaction(), updateBookStock()
 *
 * DIP: Dependency (TransactionDatabase, BookDatabase) diterima via parent getter,
 *   bukan akses field langsung. CartGUI tidak perlu tahu detail internal parent.
 *
 * OCP: Method validateAllStock() dan createTransaction() bisa di-override
 *   oleh subclass untuk aturan bisnis berbeda tanpa modifikasi kode ini.
 */
public class CartGUI extends JDialog {
    private Cart cart;
    private TransactionDatabase transactionDatabase;
    private BookDatabase bookDatabase;
    private BookStoreGUI parentGUI;
    private String customerId;
    private JTable cartTable;
    private DefaultTableModel tableModel;

    /**
     * DIP: Dependency diterima melalui konstruktor, bukan dibuat sendiri.
     */
    public CartGUI(Cart cart, BookStoreGUI parent, String customerId) {
        super(parent, "Keranjang", true);
        this.cart = cart;
        this.parentGUI = parent;
        this.customerId = customerId;
        this.transactionDatabase = parent.getTransactionDatabase();
        this.bookDatabase = parent.getBookDatabase();

        initializeUI();
        setVisible(true);
    }

    // SRP: Setiap method UI punya satu tanggung jawab
    private void initializeUI() {
        setLayout(new BorderLayout());
        setSize(600, 400);
        initializeCartTable();
        initializeButtonPanel();
    }

    private void initializeCartTable() {
        tableModel = new DefaultTableModel(new String[]{"Judul Buku", "Jumlah", "Subtotal"}, 0);
        cartTable = new JTable(tableModel);
        loadCartToTable();
        add(new JScrollPane(cartTable), BorderLayout.CENTER);
    }

    private void initializeButtonPanel() {
        JPanel buttonPanel = new JPanel();
        JButton completeButton = new JButton("Selesai");
        JButton cancelButton = new JButton("Batal");
        buttonPanel.add(completeButton);
        buttonPanel.add(cancelButton);
        add(buttonPanel, BorderLayout.SOUTH);
        completeButton.addActionListener(e -> completeTransaction());
        cancelButton.addActionListener(e -> dispose());
    }

    private void loadCartToTable() {
        tableModel.setRowCount(0);
        for (CartItem item : cart.getItems()) {
            tableModel.addRow(new Object[]{item.getBookTitle(), item.getQuantity(), item.getSubtotal()});
        }
    }

    // SRP: Alur transaksi dipecah menjadi langkah-langkah kecil
    private void completeTransaction() {
        List<CartItem> items = cart.getItems();
        if (!validateAllStock(items)) return;

        for (CartItem item : items) {
            Transaction transaction = createTransaction(item);
            saveTransaction(transaction);
            updateBookStock(item);
        }

        JOptionPane.showMessageDialog(this, "Transaksi berhasil diselesaikan!", "Success", JOptionPane.INFORMATION_MESSAGE);
        cart.clear();
        parentGUI.loadBooksToTable();
        dispose();
    }

    /** SRP: HANYA validasi stok. OCP: bisa di-override untuk aturan berbeda. */
    protected boolean validateAllStock(List<CartItem> items) {
        for (CartItem item : items) {
            Book book = findBookByTitle(item.getBookTitle());
            if (book != null && book.getStock() < item.getQuantity()) {
                JOptionPane.showMessageDialog(this, "Stok tidak cukup untuk " + book.getTitle(), "Error", JOptionPane.ERROR_MESSAGE);
                return false;
            }
        }
        return true;
    }

    /** SRP: HANYA membuat objek Transaction. */
    protected Transaction createTransaction(CartItem item) {
        return new Transaction("ORD" + System.currentTimeMillis(), "TRANS" + System.currentTimeMillis(),
                customerId, item.getBookTitle(), item.getQuantity(), item.getSubtotal());
    }

    /** SRP: HANYA menyimpan transaksi. DIP: menggunakan dependency yang di-inject. */
    protected void saveTransaction(Transaction transaction) {
        transactionDatabase.addTransaction(transaction);
    }

    /** SRP: HANYA update stok buku. */
    protected void updateBookStock(CartItem item) {
        Book book = findBookByTitle(item.getBookTitle());
        if (book != null) {
            book.setStock(book.getStock() - item.getQuantity());
            bookDatabase.updateBook(bookDatabase.getBooks().indexOf(book), book);
        }
    }

    /** SRP: Helper pencarian buku, menghindari duplikasi loop. */
    private Book findBookByTitle(String title) {
        for (Book book : bookDatabase.getBooks()) {
            if (book.getTitle().equals(title)) return book;
        }
        return null;
    }
}

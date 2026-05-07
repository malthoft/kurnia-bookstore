package org.example.uapproglans3;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class BookStoreGUI extends JFrame {
    protected BookDatabase bookDatabase;
    protected CustomerDatabase customerDatabase;
    protected TransactionDatabase transactionDatabase;
    protected Cart cart;
    JTable bookTable;
    private DefaultTableModel tableModel;

    public BookStoreGUI() {
        bookDatabase = new BookDatabase();
        customerDatabase = new CustomerDatabase();
        transactionDatabase = new TransactionDatabase();
        cart = new Cart();

        setTitle("Toko Buku Kurnia");
        setSize(1000, 700);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Header
        JLabel headerLabel = new JLabel("Toko Buku Kurnia", SwingConstants.CENTER);
        headerLabel.setFont(new Font("Serif", Font.BOLD, 32));
        headerLabel.setForeground(Color.BLUE);
        headerLabel.setBorder(BorderFactory.createEmptyBorder(20, 10, 20, 10));
        add(headerLabel, BorderLayout.NORTH);

        // Tabel Buku
        tableModel = new DefaultTableModel(new String[]{"Judul", "Penulis", "Penerbit", "Tahun", "Stok", "Harga", "Gambar"}, 0);
        bookTable = new JTable(tableModel) {
            @Override
            public Class<?> getColumnClass(int columnIndex) {
                return columnIndex == 6 ? ImageIcon.class : super.getColumnClass(columnIndex);
            }
        };
        bookTable.setRowHeight(120);
        loadBooksToTable();
        add(new JScrollPane(bookTable), BorderLayout.CENTER);

        // Panel Tombol
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(2, 3, 15, 15));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JButton addButton = createButton("Tambah Buku", "Menambahkan buku baru ke toko.");
        JButton updateButton = createButton("Perbarui Buku", "Memperbarui informasi buku yang dipilih.");
        JButton deleteButton = createButton("Hapus Buku", "Menghapus buku dari toko.");
        JButton transactionButton = createButton("Transaksi", "Melakukan transaksi pembelian buku.");
        JButton reportButton = createButton("Laporan Penjualan", "Melihat laporan penjualan.");
        JButton customerButton = createButton("Data Pelanggan", "Mengelola data pelanggan.");

        buttonPanel.add(addButton);
        buttonPanel.add(updateButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(transactionButton);
        buttonPanel.add(reportButton);
        buttonPanel.add(customerButton);

        add(buttonPanel, BorderLayout.SOUTH);

        // Action Listeners
        addButton.addActionListener(e -> new BookManagementGUI(this, "Tambah Buku", null));
        updateButton.addActionListener(e -> {
            int selectedRow = bookTable.getSelectedRow();
            if (selectedRow != -1) {
                new BookManagementGUI(this, "Perbarui Buku", bookDatabase.getBooks().get(selectedRow));
            } else {
                JOptionPane.showMessageDialog(this, "Pilih buku yang ingin diperbarui!", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
        deleteButton.addActionListener(e -> deleteBook());
        transactionButton.addActionListener(e -> new TransactionGUI(this));
        reportButton.addActionListener(e -> new SalesReportGUI(this));
        customerButton.addActionListener(e -> new CustomerManagementGUI(customerDatabase));

        setVisible(true);
    }

    private JButton createButton(String text, String tooltip) {
        JButton button = new JButton(text);
        button.setFont(new Font("SansSerif", Font.PLAIN, 18));
        button.setToolTipText(tooltip);
        return button;
    }

    public void loadBooksToTable() {
        tableModel.setRowCount(0);
        for (Book book : bookDatabase.getBooks()) {
            ImageIcon imageIcon = new ImageIcon(new ImageIcon(book.getImagePath()).getImage().getScaledInstance(100, 120, Image.SCALE_SMOOTH));
            tableModel.addRow(new Object[]{book.getTitle(), book.getAuthor(), book.getPublisher(), book.getYear(), book.getStock(), book.getPrice(), imageIcon});
        }
    }

    public void addBook(Book book) {
        bookDatabase.addBook(book);
        loadBooksToTable();
    }

    public void updateBook(int index, Book book) {
        bookDatabase.updateBook(index, book);
        loadBooksToTable();
    }

    public void deleteBook() {
        int selectedRow = bookTable.getSelectedRow();
        if (selectedRow != -1) {
            bookDatabase.deleteBook(selectedRow);
            loadBooksToTable();
        } else {
            JOptionPane.showMessageDialog(this, "Pilih buku yang ingin dihapus!", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public DefaultTableModel getBookTableModel() {
        return tableModel;
    }

    public CustomerDatabase getCustomerDatabase() {
        return customerDatabase;
    }

    public TransactionDatabase getTransactionDatabase() {
        return transactionDatabase;
    }

    public Cart getCart() {
        return cart;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(BookStoreGUI::new);
    }
}

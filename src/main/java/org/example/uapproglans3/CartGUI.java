package org.example.uapproglans3;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class CartGUI extends JDialog {
    private Cart cart;
    private BookStoreGUI parent;
    private JTable cartTable;
    private DefaultTableModel tableModel;
    private String customerId; // Menyimpan ID pelanggan

    public CartGUI(Cart cart, BookStoreGUI parent, String customerId) {
        super(parent, "Keranjang", true);
        this.cart = cart;
        this.parent = parent;
        this.customerId = customerId; // Simpan ID pelanggan

        setLayout(new BorderLayout());
        setSize(600, 400);

        // Table
        tableModel = new DefaultTableModel(new String[]{"Judul Buku", "Jumlah", "Subtotal"}, 0);
        cartTable = new JTable(tableModel);
        loadCartToTable();
        add(new JScrollPane(cartTable), BorderLayout.CENTER);

        // Button Panel
        JPanel buttonPanel = new JPanel();
        JButton completeButton = new JButton("Selesai");
        JButton cancelButton = new JButton("Batal");
        buttonPanel.add(completeButton);
        buttonPanel.add(cancelButton);
        add(buttonPanel, BorderLayout.SOUTH);

        completeButton.addActionListener(e -> completeTransaction());
        cancelButton.addActionListener(e -> dispose());

        setVisible(true);
    }

    private void loadCartToTable() {
        tableModel.setRowCount(0);
        for (CartItem item : cart.getItems()) {
            tableModel.addRow(new Object[]{item.getBookTitle(), item.getQuantity(), item.getSubtotal()});
        }
    }

    private void completeTransaction() {
        // Logika untuk menyimpan transaksi ke database
        for (CartItem item : cart.getItems()) {
            // Simpan transaksi ke database
            Transaction transaction = new Transaction(
                    "ORD" + System.currentTimeMillis(),
                    "TRANS" + System.currentTimeMillis(),
                    customerId, // Menggunakan ID pelanggan yang disimpan
                    item.getBookTitle(), // Menyimpan judul buku
                    item.getQuantity(),
                    item.getSubtotal()
            );
            parent.getTransactionDatabase().addTransaction(transaction); // Simpan transaksi ke database

            // Kurangi stok buku
            for (Book book : parent.bookDatabase.getBooks()) {
                if (book.getTitle().equals(item.getBookTitle())) {
                    int newStock = book.getStock() - item.getQuantity();
                    if (newStock < 0) {
                        JOptionPane.showMessageDialog(this, "Stok tidak cukup untuk " + book.getTitle(), "Error", JOptionPane.ERROR_MESSAGE);
                        return; // Jika stok tidak cukup, batalkan transaksi
                    }
                    book.setStock(newStock);
                    parent.bookDatabase.updateBook(parent.bookDatabase.getBooks().indexOf(book), book); // Update stok buku
                    break;
                }
            }
        }
        JOptionPane.showMessageDialog(this, "Transaksi berhasil diselesaikan!", "Success", JOptionPane.INFORMATION_MESSAGE);
        cart.clear(); // Kosongkan keranjang setelah transaksi selesai
        parent.loadBooksToTable(); // Memperbarui tabel buku di BookStoreGUI
        dispose();
    }
}

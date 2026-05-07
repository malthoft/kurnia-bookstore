package org.example.uapproglans3;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class TransactionGUI extends JDialog {
    private JTextField transactionCodeField, bookCodeField, quantityField;
    private JComboBox<String> customerComboBox; // Dropdown untuk pelanggan
    private BookStoreGUI parent;

    public TransactionGUI(BookStoreGUI parent) {
        super(parent, "Transaksi", true);
        this.parent = parent;

        // Set layout and size
        setLayout(new GridBagLayout());
        setSize(600, 500);
        setLocationRelativeTo(parent); // Center


        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(10, 10, 10, 10); // Add padding

        // Title
        JLabel titleLabel = new JLabel("Transaksi Buku");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        titleLabel.setForeground(Color.BLUE);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2; // Span across two columns
        add(titleLabel, gbc);

        // Kode Transaksi
        gbc.gridwidth = 1; // Reset to default
        gbc.gridx = 0;
        gbc.gridy = 1;
        add(new JLabel("Kode Transaksi:"), gbc);
        transactionCodeField = new JTextField("TRANS" + System.currentTimeMillis());
        transactionCodeField.setEditable(false);
        gbc.gridx = 1;
        add(transactionCodeField, gbc);

        // Kode Pelanggan
        gbc.gridx = 0;
        gbc.gridy = 2;
        add(new JLabel("Kode Pelanggan:"), gbc);
        customerComboBox = new JComboBox<>();
        loadCustomersToComboBox(); // Load customers into the combo box
        gbc.gridx = 1;
        add(customerComboBox, gbc);

        // Kode Buku
        gbc.gridx = 0;
        gbc.gridy = 3;
        add(new JLabel("Kode Buku:"), gbc);
        bookCodeField = new JTextField();
        bookCodeField.setEditable(false); // Kode buku tidak bisa diedit
        gbc.gridx = 1;
        add(bookCodeField, gbc);

        // Jumlah
        gbc.gridx = 0;
        gbc.gridy = 4;
        add(new JLabel("Jumlah:"), gbc);
        quantityField = new JTextField();
        gbc.gridx = 1;
        add(quantityField, gbc);

        // Button Panel
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout());

        JButton addToCartButton = new JButton("Tambah ke Keranjang");
        JButton viewCartButton = new JButton("Lihat Keranjang");
        buttonPanel.add(addToCartButton);
        buttonPanel.add(viewCartButton);

        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.gridwidth = 2; // Span across two columns
        add(buttonPanel, gbc);

        // Tabel Buku
        JTable bookTable = new JTable(parent.getBookTableModel());
        bookTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        bookTable.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                int row = bookTable.getSelectedRow();
                if (row != -1) {
                    String bookCode = (String) bookTable.getValueAt(row, 0); // Ambil kode buku dari tabel
                    bookCodeField.setText(bookCode); // Set kode buku ke field
                }
            }
        });

        // Mengatur lebar kolom tabel
        bookTable.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        JScrollPane scrollPane = new JScrollPane(bookTable);
        scrollPane.setPreferredSize(new Dimension(550, 150)); // Mengatur ukuran scroll pane

        gbc.gridx = 0;
        gbc.gridy = 6;
        gbc.gridwidth = 2; // Span across two columns
        add(scrollPane, gbc);

        // Action Listeners
        addToCartButton.addActionListener(e -> addToCart());
        viewCartButton.addActionListener(e -> viewCart());

        setVisible(true);
    }

    private void loadCustomersToComboBox() {
        for (Customer customer : parent.customerDatabase.getCustomers()) {
            customerComboBox.addItem(customer.getId() + " - " + customer.getName());
        }
    }

    private void addToCart() {
        String transactionCode = transactionCodeField.getText();
        String selectedCustomer = (String) customerComboBox.getSelectedItem();
        String customerId = selectedCustomer.split(" - ")[0]; // Ambil ID pelanggan
        String bookCode = bookCodeField.getText();
        int quantity = Integer.parseInt(quantityField.getText());

        // Ambil informasi buku dari database
        for (Book book : parent.bookDatabase.getBooks()) {
            if (book.getTitle().equals(bookCode)) {
                CartItem item = new CartItem(book.getTitle(), book.getTitle(), quantity, book.getPrice());
                parent.getCart().addItem(item); // Tambahkan item ke keranjang
                JOptionPane.showMessageDialog(this, "Buku ditambahkan ke keranjang!", "Success", JOptionPane.INFORMATION_MESSAGE);
                return;
            }
        }
        JOptionPane.showMessageDialog(this, "Buku tidak ditemukan!", "Error", JOptionPane.ERROR_MESSAGE);
    }

    private void viewCart() {
        new CartGUI(parent.getCart(), parent, customerComboBox.getSelectedItem().toString()); // Tampilkan keranjang dengan ID pelanggan
    }
}

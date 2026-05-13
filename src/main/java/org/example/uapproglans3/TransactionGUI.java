package org.example.uapproglans3;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

/**
 * TransactionGUI - GUI untuk proses transaksi pembelian buku.
 *
 * === PENERAPAN PRINSIP SOLID ===
 *
 * SRP: Konstruktor monolitik dipecah menjadi method fokus:
 *   initializeUI(), initializeFormFields(), initializeButtonPanel(), initializeBookTable()
 *   Business logic dipisah: findBookByTitle(), addToCart(), viewCart()
 *
 * DIP: Mengakses dependency melalui getter parent (getCustomerDatabase(),
 *   getBookDatabase(), getCart()), bukan field internal langsung.
 *
 * OCP: Method addToCart() dan findBookByTitle() bisa di-override subclass
 *   untuk logika bisnis berbeda tanpa modifikasi kode ini.
 */
public class TransactionGUI extends JDialog {
    private JTextField transactionCodeField, bookCodeField, quantityField;
    private JComboBox<String> customerComboBox;
    private BookStoreGUI parent;

    public TransactionGUI(BookStoreGUI parent) {
        super(parent, "Transaksi", true);
        this.parent = parent;

        // SRP: Setup UI didelegasikan ke method terpisah
        initializeUI();
        setVisible(true);
    }

    // ==================== UI INITIALIZATION (SRP) ====================

    /** SRP: HANYA mengatur layout dasar dan mendelegasikan ke method spesifik. */
    private void initializeUI() {
        setLayout(new GridBagLayout());
        setSize(600, 500);
        setLocationRelativeTo(parent);

        initializeTitle();
        initializeFormFields();
        initializeButtonPanel();
        initializeBookTable();
    }

    /** SRP: HANYA membuat judul halaman transaksi. */
    private void initializeTitle() {
        GridBagConstraints gbc = createGBC(0, 0);
        gbc.gridwidth = 2;
        JLabel titleLabel = new JLabel("Transaksi Buku");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        titleLabel.setForeground(Color.BLUE);
        add(titleLabel, gbc);
    }

    /** SRP: HANYA membuat form fields (kode transaksi, pelanggan, buku, jumlah). */
    private void initializeFormFields() {
        // Kode Transaksi
        GridBagConstraints gbc = createGBC(0, 1);
        add(new JLabel("Kode Transaksi:"), gbc);
        transactionCodeField = new JTextField("TRANS" + System.currentTimeMillis());
        transactionCodeField.setEditable(false);
        gbc = createGBC(1, 1);
        add(transactionCodeField, gbc);

        // Kode Pelanggan (DIP: akses via getter, bukan field langsung)
        gbc = createGBC(0, 2);
        add(new JLabel("Kode Pelanggan:"), gbc);
        customerComboBox = new JComboBox<>();
        loadCustomersToComboBox();
        gbc = createGBC(1, 2);
        add(customerComboBox, gbc);

        // Kode Buku
        gbc = createGBC(0, 3);
        add(new JLabel("Kode Buku:"), gbc);
        bookCodeField = new JTextField();
        bookCodeField.setEditable(false);
        gbc = createGBC(1, 3);
        add(bookCodeField, gbc);

        // Jumlah
        gbc = createGBC(0, 4);
        add(new JLabel("Jumlah:"), gbc);
        quantityField = new JTextField();
        gbc = createGBC(1, 4);
        add(quantityField, gbc);
    }

    /** SRP: HANYA membuat panel tombol aksi. */
    private void initializeButtonPanel() {
        JPanel buttonPanel = new JPanel(new FlowLayout());
        JButton addToCartButton = new JButton("Tambah ke Keranjang");
        JButton viewCartButton = new JButton("Lihat Keranjang");
        buttonPanel.add(addToCartButton);
        buttonPanel.add(viewCartButton);

        GridBagConstraints gbc = createGBC(0, 5);
        gbc.gridwidth = 2;
        add(buttonPanel, gbc);

        addToCartButton.addActionListener(e -> addToCart());
        viewCartButton.addActionListener(e -> viewCart());
    }

    /** SRP: HANYA membuat tabel daftar buku untuk dipilih. */
    private void initializeBookTable() {
        JTable bookTable = new JTable(parent.getBookTableModel());
        bookTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        bookTable.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                int row = bookTable.getSelectedRow();
                if (row != -1) {
                    String bookCode = (String) bookTable.getValueAt(row, 0);
                    bookCodeField.setText(bookCode);
                }
            }
        });

        bookTable.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        JScrollPane scrollPane = new JScrollPane(bookTable);
        scrollPane.setPreferredSize(new Dimension(550, 150));

        GridBagConstraints gbc = createGBC(0, 6);
        gbc.gridwidth = 2;
        add(scrollPane, gbc);
    }

    // ==================== HELPER METHODS (SRP) ====================

    /** SRP: Factory method untuk GridBagConstraints, menghindari duplikasi kode. */
    private GridBagConstraints createGBC(int gridx, int gridy) {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.gridx = gridx;
        gbc.gridy = gridy;
        gbc.gridwidth = 1;
        return gbc;
    }

    // ==================== BUSINESS LOGIC (SRP + DIP) ====================

    /**
     * SRP: HANYA memuat data pelanggan ke combo box.
     * DIP: Mengakses CustomerDatabase melalui getter parent,
     *   bukan field internal langsung (parent.customerDatabase).
     */
    private void loadCustomersToComboBox() {
        for (Customer customer : parent.getCustomerDatabase().getCustomers()) {
            customerComboBox.addItem(customer.getId() + " - " + customer.getName());
        }
    }

    /**
     * SRP: HANYA menangani logika penambahan buku ke keranjang.
     * Terpisah dari UI setup dan event handling.
     *
     * OCP: Bisa di-override untuk aturan bisnis berbeda
     *   (misal: cek minimum order, diskon quantity).
     */
    protected void addToCart() {
        String selectedCustomer = (String) customerComboBox.getSelectedItem();
        if (selectedCustomer == null) {
            showErrorMessage("Pilih pelanggan terlebih dahulu!");
            return;
        }

        String bookCode = bookCodeField.getText();
        if (bookCode.isEmpty()) {
            showErrorMessage("Pilih buku dari tabel terlebih dahulu!");
            return;
        }

        // SRP: Validasi input dipisahkan dari logika bisnis
        int quantity = parseQuantity();
        if (quantity <= 0) return;

        // SRP: Pencarian buku didelegasikan ke method terpisah
        Book book = findBookByTitle(bookCode);
        if (book != null) {
            CartItem item = new CartItem(book.getTitle(), book.getTitle(), quantity, book.getPrice());
            parent.getCart().addItem(item);
            JOptionPane.showMessageDialog(this, "Buku ditambahkan ke keranjang!", "Success", JOptionPane.INFORMATION_MESSAGE);
        } else {
            showErrorMessage("Buku tidak ditemukan!");
        }
    }

    /** SRP: HANYA validasi dan parsing input jumlah. */
    private int parseQuantity() {
        try {
            return Integer.parseInt(quantityField.getText());
        } catch (NumberFormatException e) {
            showErrorMessage("Jumlah harus berupa angka!");
            return -1;
        }
    }

    /**
     * SRP: HANYA mencari buku berdasarkan judul.
     * DIP: Mengakses BookDatabase melalui getter parent.
     */
    protected Book findBookByTitle(String title) {
        for (Book book : parent.getBookDatabase().getBooks()) {
            if (book.getTitle().equals(title)) return book;
        }
        return null;
    }

    /** SRP: HANYA membuka dialog keranjang. */
    private void viewCart() {
        new CartGUI(parent.getCart(), parent, customerComboBox.getSelectedItem().toString());
    }

    /** SRP: Utilitas pesan error, menghindari duplikasi JOptionPane. */
    private void showErrorMessage(String message) {
        JOptionPane.showMessageDialog(this, message, "Error", JOptionPane.ERROR_MESSAGE);
    }
}

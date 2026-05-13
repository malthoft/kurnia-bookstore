package org.example.uapproglans3;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

/**
 * BookStoreGUI - Kelas utama GUI untuk aplikasi Toko Buku Kurnia.
 *
 * === PENERAPAN PRINSIP SOLID ===
 *
 * 1. SRP (Single Responsibility Principle):
 *    - SEBELUM: Kelas ini adalah "God Class" yang menangani SEMUA hal:
 *      inisialisasi database, render UI, manajemen buku, dan navigasi.
 *      Ini melanggar SRP karena ada banyak "alasan untuk berubah".
 *    - SESUDAH: Tanggung jawab dipecah menjadi method-method yang fokus:
 *      * initializeDatabases() → hanya inisialisasi data
 *      * initializeUI() → hanya setup tampilan utama
 *      * initializeHeader() → hanya setup header
 *      * initializeBookTable() → hanya setup tabel buku
 *      * initializeButtonPanel() → hanya setup panel tombol
 *      * registerEventListeners() → hanya setup event handler
 *      Setiap method punya SATU alasan untuk berubah.
 *
 * 2. DIP (Dependency Inversion Principle):
 *    - SEBELUM: Langsung membuat objek konkret dengan `new BookDatabase()`,
 *      `new CustomerDatabase()`, dst. → Tight coupling ke implementasi spesifik.
 *      Jika ingin mengganti database (misal ke MySQL), harus ubah kelas ini.
 *    - SESUDAH: Ditambahkan Constructor Injection. Dependency (BookDatabase,
 *      CustomerDatabase, TransactionDatabase, Cart) bisa disuntikkan dari luar
 *      melalui konstruktor. Konstruktor default tetap ada untuk backward compatibility.
 *      Ini memungkinkan pengujian dan penggantian implementasi tanpa modifikasi.
 *
 * 3. OCP (Open/Closed Principle):
 *    - SEBELUM: Menambah fitur baru (misal tombol baru) memaksa modifikasi
 *      langsung di konstruktor yang sudah panjang dan kompleks.
 *    - SESUDAH: Dengan method yang terpisah dan terstruktur, fitur baru bisa
 *      ditambahkan dengan meng-override method spesifik di subclass tanpa
 *      mengubah kode yang sudah ada.
 */
public class BookStoreGUI extends JFrame {

    // ==================== FIELD DECLARATIONS ====================
    // SRP: Field dikelompokkan berdasarkan tanggung jawab

    // Data layer dependencies (DIP: bisa di-inject melalui konstruktor)
    protected BookDatabase bookDatabase;
    protected CustomerDatabase customerDatabase;
    protected TransactionDatabase transactionDatabase;
    protected Cart cart;

    // UI components (SRP: hanya terkait tampilan)
    JTable bookTable;
    private DefaultTableModel tableModel;

    // ==================== CONSTRUCTORS ====================

    /**
     * Konstruktor default - tetap dipertahankan untuk backward compatibility.
     * Menggunakan implementasi konkret sebagai default.
     *
     * DIP: Konstruktor ini mendelegasikan ke konstruktor dengan parameter,
     * menyediakan implementasi default. Kelas lain yang membutuhkan
     * implementasi berbeda bisa menggunakan konstruktor berparameter.
     */
    public BookStoreGUI() {
        this(new BookDatabase(), new CustomerDatabase(), new TransactionDatabase(), new Cart());
    }

    /**
     * Konstruktor dengan Dependency Injection.
     *
     * DIP (Dependency Inversion Principle):
     * - Modul tingkat tinggi (BookStoreGUI) TIDAK LAGI membuat sendiri
     *   modul tingkat rendah (BookDatabase, CustomerDatabase, dll).
     * - Dependency disuntikkan dari luar melalui parameter konstruktor.
     * - Analoginya seperti stopkontak: BookStoreGUI hanya perlu tahu
     *   "bentuk colokan" (tipe parameter), bukan "pembangkit listrik"-nya.
     *
     * @param bookDatabase        Database untuk manajemen buku
     * @param customerDatabase    Database untuk manajemen pelanggan
     * @param transactionDatabase Database untuk manajemen transaksi
     * @param cart                Keranjang belanja
     */
    public BookStoreGUI(BookDatabase bookDatabase, CustomerDatabase customerDatabase,
                        TransactionDatabase transactionDatabase, Cart cart) {
        // DIP: Menyimpan dependency yang di-inject, bukan membuat sendiri
        this.bookDatabase = bookDatabase;
        this.customerDatabase = customerDatabase;
        this.transactionDatabase = transactionDatabase;
        this.cart = cart;

        // SRP: Setiap method hanya punya SATU tanggung jawab
        initializeUI();
    }

    // ==================== UI INITIALIZATION (SRP) ====================
    // Setiap method di bawah ini hanya bertanggung jawab atas SATU aspek UI.
    // Ini menerapkan SRP: jika layout header berubah, hanya initializeHeader()
    // yang perlu diubah. Jika tabel berubah, hanya initializeBookTable().

    /**
     * SRP: Method ini HANYA bertanggung jawab untuk setup frame utama
     * dan mendelegasikan ke method-method spesifik.
     */
    private void initializeUI() {
        setTitle("Toko Buku Kurnia");
        setSize(1000, 700);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        initializeHeader();
        initializeBookTable();
        initializeButtonPanel();

        setVisible(true);
    }

    /**
     * SRP: Method ini HANYA bertanggung jawab untuk membuat header.
     * Jika desain header berubah, hanya method ini yang perlu dimodifikasi.
     */
    private void initializeHeader() {
        JLabel headerLabel = new JLabel("Toko Buku Kurnia", SwingConstants.CENTER);
        headerLabel.setFont(new Font("Serif", Font.BOLD, 32));
        headerLabel.setForeground(Color.BLUE);
        headerLabel.setBorder(BorderFactory.createEmptyBorder(20, 10, 20, 10));
        add(headerLabel, BorderLayout.NORTH);
    }

    /**
     * SRP: Method ini HANYA bertanggung jawab untuk setup tabel buku.
     * Perubahan pada struktur tabel hanya mempengaruhi method ini.
     */
    private void initializeBookTable() {
        tableModel = new DefaultTableModel(
                new String[]{"Judul", "Penulis", "Penerbit", "Tahun", "Stok", "Harga", "Gambar"}, 0);
        bookTable = new JTable(tableModel) {
            @Override
            public Class<?> getColumnClass(int columnIndex) {
                return columnIndex == 6 ? ImageIcon.class : super.getColumnClass(columnIndex);
            }
        };
        bookTable.setRowHeight(120);
        loadBooksToTable();
        add(new JScrollPane(bookTable), BorderLayout.CENTER);
    }

    /**
     * SRP: Method ini HANYA bertanggung jawab untuk panel tombol dan event-nya.
     *
     * OCP (Open/Closed Principle):
     * Method ini menggunakan helper createButton() yang bisa di-extend.
     * Jika ingin menambah tombol baru, cukup tambahkan di sini
     * tanpa mengubah method lain (header, tabel, dll).
     */
    private void initializeButtonPanel() {
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

        // SRP: Event listener di-register terpisah, jelas dan mudah di-maintain
        registerEventListeners(addButton, updateButton, deleteButton,
                transactionButton, reportButton, customerButton);
    }

    /**
     * SRP: Method ini HANYA bertanggung jawab untuk mendaftarkan event listener.
     * Memisahkan "apa yang ditampilkan" dari "apa yang terjadi saat diklik".
     */
    private void registerEventListeners(JButton addButton, JButton updateButton,
                                        JButton deleteButton, JButton transactionButton,
                                        JButton reportButton, JButton customerButton) {
        addButton.addActionListener(e -> new BookManagementGUI(this, "Tambah Buku", null));
        updateButton.addActionListener(e -> handleUpdateBook());
        deleteButton.addActionListener(e -> deleteBook());
        transactionButton.addActionListener(e -> new TransactionGUI(this));
        reportButton.addActionListener(e -> new SalesReportGUI(this));
        customerButton.addActionListener(e -> new CustomerManagementGUI(customerDatabase));
    }

    // ==================== BUSINESS LOGIC (SRP) ====================
    // Method-method di bawah ini menangani logika bisnis secara terpisah dari UI.

    /**
     * OCP: Method createButton() bersifat reusable.
     * Jika ingin mengubah style tombol secara global, cukup ubah di sini.
     * Subclass juga bisa override method ini untuk tampilan berbeda.
     */
    private JButton createButton(String text, String tooltip) {
        JButton button = new JButton(text);
        button.setFont(new Font("SansSerif", Font.PLAIN, 18));
        button.setToolTipText(tooltip);
        return button;
    }

    /**
     * SRP: Method ini HANYA bertanggung jawab untuk memuat data buku ke tabel.
     * Satu alasan berubah: jika format tampilan data buku berubah.
     */
    public void loadBooksToTable() {
        tableModel.setRowCount(0);
        for (Book book : bookDatabase.getBooks()) {
            ImageIcon imageIcon = new ImageIcon(
                    new ImageIcon(book.getImagePath()).getImage()
                            .getScaledInstance(100, 120, Image.SCALE_SMOOTH));
            tableModel.addRow(new Object[]{
                    book.getTitle(), book.getAuthor(), book.getPublisher(),
                    book.getYear(), book.getStock(), book.getPrice(), imageIcon
            });
        }
    }

    /**
     * SRP: Method ini HANYA bertanggung jawab untuk menambah buku.
     * Satu alasan berubah: jika proses penambahan buku berubah.
     */
    public void addBook(Book book) {
        bookDatabase.addBook(book);
        loadBooksToTable();
    }

    /**
     * SRP: Method ini HANYA bertanggung jawab untuk memperbarui buku.
     */
    public void updateBook(int index, Book book) {
        bookDatabase.updateBook(index, book);
        loadBooksToTable();
    }

    /**
     * SRP: Method ini HANYA menangani logika pemilihan buku untuk update.
     * Memisahkan validasi seleksi dari proses update itu sendiri.
     */
    private void handleUpdateBook() {
        int selectedRow = bookTable.getSelectedRow();
        if (selectedRow != -1) {
            new BookManagementGUI(this, "Perbarui Buku", bookDatabase.getBooks().get(selectedRow));
        } else {
            showErrorMessage("Pilih buku yang ingin diperbarui!");
        }
    }

    /**
     * SRP: Method ini HANYA bertanggung jawab untuk menghapus buku.
     * Satu alasan berubah: jika proses penghapusan buku berubah.
     */
    public void deleteBook() {
        int selectedRow = bookTable.getSelectedRow();
        if (selectedRow != -1) {
            bookDatabase.deleteBook(selectedRow);
            loadBooksToTable();
        } else {
            showErrorMessage("Pilih buku yang ingin dihapus!");
        }
    }

    /**
     * SRP: Method utilitas untuk menampilkan pesan error.
     * Menghindari duplikasi kode JOptionPane di banyak tempat.
     */
    private void showErrorMessage(String message) {
        JOptionPane.showMessageDialog(this, message, "Error", JOptionPane.ERROR_MESSAGE);
    }

    // ==================== GETTERS (DIP Support) ====================
    // Getter menyediakan akses terkontrol ke dependency,
    // mendukung prinsip DIP di kelas-kelas lain yang membutuhkannya.

    public DefaultTableModel getBookTableModel() {
        return tableModel;
    }

    public BookDatabase getBookDatabase() {
        return bookDatabase;
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

    // ==================== ENTRY POINT ====================

    public static void main(String[] args) {
        SwingUtilities.invokeLater(BookStoreGUI::new);
    }
}

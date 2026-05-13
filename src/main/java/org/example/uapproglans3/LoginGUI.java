package org.example.uapproglans3;

import javax.swing.*;
import java.awt.*;

/**
 * LoginGUI - Kelas GUI untuk halaman login aplikasi Toko Buku Kurnia.
 *
 * === PENERAPAN PRINSIP SOLID ===
 *
 * 1. SRP (Single Responsibility Principle):
 *    - SEBELUM: Konstruktor menangani SEMUA hal sekaligus: setup header,
 *      setup form, setup tombol, logika autentikasi, dan navigasi.
 *      Jika ada perubahan desain header ATAU perubahan cara login,
 *      konstruktor yang sama harus diubah → banyak alasan untuk berubah.
 *    - SESUDAH: Tanggung jawab dipecah menjadi method spesifik:
 *      * initializeUI() → mengatur window utama
 *      * initializeHeader() → HANYA membuat header/logo
 *      * initializeForm() → HANYA membuat form input
 *      * initializeButtons() → HANYA membuat tombol
 *      * authenticate() → HANYA logika validasi kredensial
 *      * onLoginSuccess() → HANYA logika setelah login berhasil
 *      * onLoginFailed() → HANYA logika setelah login gagal
 *      Sekarang setiap method punya SATU alasan untuk berubah.
 *
 * 2. OCP (Open/Closed Principle):
 *    - SEBELUM: Logika autentikasi di-hardcode di dalam lambda listener.
 *      Untuk mengubah mekanisme autentikasi (misal dari hardcode ke database),
 *      harus MEMODIFIKASI kode yang sudah ada → melanggar OCP.
 *    - SESUDAH: Method authenticate() di-extract sebagai method terpisah
 *      yang bisa di-OVERRIDE oleh subclass tanpa mengubah kode LoginGUI.
 *      Contoh: class DatabaseLoginGUI extends LoginGUI bisa override
 *      authenticate() untuk cek ke database, tanpa ubah LoginGUI sama sekali.
 *      Seperti stopkontak: "tertutup" untuk modifikasi kabel internal,
 *      tapi "terbuka" untuk colokan baru (subclass baru).
 *
 * 3. DIP (Dependency Inversion Principle):
 *    - SEBELUM: Langsung memanggil `new BookStoreGUI()` di dalam listener.
 *      LoginGUI "bergantung" langsung pada kelas konkret BookStoreGUI.
 *    - SESUDAH: Navigasi setelah login berhasil diisolasi di method
 *      onLoginSuccess() yang bisa di-override. Ini mengurangi tight coupling
 *      dan memungkinkan pengujian tanpa membuka BookStoreGUI yang sebenarnya.
 */
public class LoginGUI extends JFrame {

    // ==================== FIELD DECLARATIONS ====================
    // SRP: Field hanya berisi komponen UI yang diperlukan untuk login
    private JTextField usernameField;
    private JPasswordField passwordField;

    // ==================== CONSTRUCTOR ====================

    public LoginGUI() {
        initializeUI();
    }

    // ==================== UI INITIALIZATION (SRP) ====================
    // Setiap method bertanggung jawab atas SATU bagian UI saja.

    /**
     * SRP: Method ini HANYA mengatur properti window utama
     * dan mendelegasikan pembuatan komponen ke method spesifik.
     */
    private void initializeUI() {
        setTitle("Kurnia BookStore - Login");
        setSize(400, 300);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        setResizable(false);

        // SRP: Setiap bagian UI dibuat oleh method yang fokus
        initializeHeader();
        initializeForm();
        initializeButtons();

        // Tampilkan GUI
        setLocationRelativeTo(null); // Pusatkan layar
        setVisible(true);
    }

    /**
     * SRP: Method ini HANYA bertanggung jawab untuk membuat header panel.
     * Satu alasan berubah: jika desain header/logo berubah.
     */
    private void initializeHeader() {
        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(new Color(50, 150, 250));
        headerPanel.setLayout(new BoxLayout(headerPanel, BoxLayout.Y_AXIS));

        JLabel logoLabel = new JLabel(new ImageIcon("path/to/logo.png"));
        JLabel titleLabel = new JLabel("Kurnia BookStore");
        titleLabel.setFont(new Font("Serif", Font.BOLD, 24));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        headerPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        headerPanel.add(logoLabel);
        headerPanel.add(titleLabel);
        headerPanel.add(Box.createRigidArea(new Dimension(0, 10)));

        add(headerPanel, BorderLayout.NORTH);
    }

    /**
     * SRP: Method ini HANYA bertanggung jawab untuk membuat form login.
     * Satu alasan berubah: jika field form berubah (misal tambah "Remember Me").
     */
    private void initializeForm() {
        JPanel formPanel = new JPanel();
        formPanel.setLayout(new GridBagLayout());
        formPanel.setBackground(Color.WHITE);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Username
        gbc.gridx = 0; gbc.gridy = 0;
        formPanel.add(new JLabel("Username:"), gbc);
        usernameField = new JTextField(20);
        gbc.gridx = 1; gbc.gridy = 0;
        formPanel.add(usernameField, gbc);

        // Password
        gbc.gridx = 0; gbc.gridy = 1;
        formPanel.add(new JLabel("Password:"), gbc);
        passwordField = new JPasswordField(20);
        gbc.gridx = 1; gbc.gridy = 1;
        formPanel.add(passwordField, gbc);

        add(formPanel, BorderLayout.CENTER);
    }

    /**
     * SRP: Method ini HANYA bertanggung jawab untuk membuat panel tombol.
     * Satu alasan berubah: jika tampilan/jumlah tombol berubah.
     */
    private void initializeButtons() {
        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(Color.WHITE);
        JButton loginButton = new JButton("Login");
        loginButton.setBackground(new Color(50, 150, 250));
        loginButton.setForeground(Color.WHITE);
        loginButton.setFocusPainted(false);

        // SRP: Listener hanya memanggil method handleLogin(),
        // tidak berisi logika bisnis secara langsung.
        loginButton.addActionListener(e -> handleLogin());

        buttonPanel.add(loginButton);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    // ==================== BUSINESS LOGIC (SRP + OCP) ====================

    /**
     * SRP: Method ini HANYA menangani alur login (mengambil input → validasi → respond).
     * Memisahkan "event handling" dari "logika autentikasi".
     */
    private void handleLogin() {
        String username = usernameField.getText();
        String password = new String(passwordField.getPassword());

        if (authenticate(username, password)) {
            onLoginSuccess();
        } else {
            onLoginFailed();
        }
    }

    /**
     * OCP (Open/Closed Principle):
     * Method ini bisa di-override oleh subclass untuk mengubah mekanisme autentikasi
     * TANPA memodifikasi kode LoginGUI yang sudah ada.
     *
     * Contoh penerapan OCP:
     * - class DatabaseLoginGUI extends LoginGUI → override authenticate()
     *   untuk cek kredensial ke database MySQL/PostgreSQL.
     * - class LDAPLoginGUI extends LoginGUI → override authenticate()
     *   untuk autentikasi via LDAP server.
     *
     * Seperti stopkontak listrik: kabel instalasi di dinding (LoginGUI) tertutup,
     * tapi bisa menerima perangkat baru (subclass) tanpa membongkar tembok.
     *
     * @param username Username yang diinputkan
     * @param password Password yang diinputkan
     * @return true jika autentikasi berhasil
     */
    protected boolean authenticate(String username, String password) {
        // Default: autentikasi sederhana (bisa di-override untuk implementasi lain)
        return username.equals("admin") && password.equals("admin");
    }

    /**
     * OCP: Method ini bisa di-override untuk mengubah perilaku setelah login berhasil.
     *
     * DIP (Dependency Inversion Principle):
     * Dengan mengisolasi pembuatan BookStoreGUI di method terpisah,
     * subclass bisa mengganti navigasi tanpa terikat pada BookStoreGUI konkret.
     */
    protected void onLoginSuccess() {
        new BookStoreGUI();
        dispose();
    }

    /**
     * OCP: Method ini bisa di-override untuk mengubah perilaku setelah login gagal.
     * Misal: menambahkan counter percobaan login, atau lockout setelah 3x gagal.
     */
    protected void onLoginFailed() {
        JOptionPane.showMessageDialog(this,
                "Username atau Password salah!", "Error", JOptionPane.ERROR_MESSAGE);
    }

    // ==================== ENTRY POINT ====================

    public static void main(String[] args) {
        SwingUtilities.invokeLater(LoginGUI::new);
    }
}

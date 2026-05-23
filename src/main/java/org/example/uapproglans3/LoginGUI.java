package org.example.uapproglans3;

import javax.swing.*;
import java.awt.*;

public class LoginGUI extends JFrame {

    private JTextField usernameField;
    private JPasswordField passwordField;


    public LoginGUI() {
        initializeUI();
    }

    private void initializeUI() {
        setTitle("Kurnia BookStore - Login");
        setSize(400, 300);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        setResizable(false);

        initializeHeader();
        initializeForm();
        initializeButtons();

        // Tampilkan GUI
        setLocationRelativeTo(null); // Pusatkan layar
        setVisible(true);
    }

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

    private void initializeButtons() {
        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(Color.WHITE);
        JButton loginButton = new JButton("Login");
        loginButton.setBackground(new Color(50, 150, 250));
        loginButton.setForeground(Color.WHITE);
        loginButton.setFocusPainted(false);

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

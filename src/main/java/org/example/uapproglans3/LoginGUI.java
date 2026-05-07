package org.example.uapproglans3;

import javax.swing.*;
import java.awt.*;

public class    LoginGUI extends JFrame {
    private JTextField usernameField;
    private JPasswordField passwordField;

    public LoginGUI() {
        setTitle("Kurnia BookStore - Login");
        setSize(400, 300);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        setResizable(false);

        // Header Panel dengan logo dan judul
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

        // Panel Form Login
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

        // Panel Tombol
        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(Color.WHITE);
        JButton loginButton = new JButton("Login");
        loginButton.setBackground(new Color(50, 150, 250));
        loginButton.setForeground(Color.WHITE);
        loginButton.setFocusPainted(false);

        loginButton.addActionListener(e -> {
            String username = usernameField.getText();
            String password = new String(passwordField.getPassword());
            if (username.equals("admin") && password.equals("admin")) {
                new BookStoreGUI();
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Username atau Password salah!", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        buttonPanel.add(loginButton);
        add(buttonPanel, BorderLayout.SOUTH);

        // Tampilkan GUI
        setLocationRelativeTo(null); // Pusatkan layar
        setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(LoginGUI::new);
    }
}

package org.example.uapproglans3;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class BookManagementGUI extends JDialog {
    private JTextField titleField, authorField, publisherField, yearField, stockField, priceField;
    private JLabel imageLabel;
    private Book book; // Assuming Book is a class that holds book details
    private boolean isUpdate;

    public BookManagementGUI(BookStoreGUI parent, String title, Book book) {
        super(parent, title, true);
        this.book = book;
        this.isUpdate = book != null;

        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);

        // Title
        gbc.gridx = 0; gbc.gridy = 0;
        add(new JLabel("Title:"), gbc);
        titleField = new JTextField();
        titleField.setPreferredSize(new Dimension(200, 25)); // Set preferred width
        titleField.setText(isUpdate ? book.getTitle() : "");
        gbc.gridx = 1; gbc.gridy = 0;
        add(titleField, gbc);

        // Author
        gbc.gridx = 0; gbc.gridy = 1;
        add(new JLabel("Author:"), gbc);
        authorField = new JTextField();
        authorField.setPreferredSize(new Dimension(200, 25)); // Set preferred width
        authorField.setText(isUpdate ? book.getAuthor() : "");
        gbc.gridx = 1; gbc.gridy = 1;
        add(authorField, gbc);

        // Publisher
        gbc.gridx = 0; gbc.gridy = 2;
        add(new JLabel("Publisher:"), gbc);
        publisherField = new JTextField();
        publisherField.setPreferredSize(new Dimension(200, 25)); // Set preferred width
        publisherField.setText(isUpdate ? book.getPublisher() : "");
        gbc.gridx = 1; gbc.gridy = 2;
        add(publisherField, gbc);

        // Year
        gbc.gridx = 0; gbc.gridy = 3;
        add(new JLabel("Year:"), gbc);
        yearField = new JTextField();
        yearField.setPreferredSize(new Dimension(200, 25)); // Set preferred width
        yearField.setText(isUpdate ? String.valueOf(book.getYear()) : "");
        gbc.gridx = 1; gbc.gridy = 3;
        add(yearField, gbc);

        // Stock
        gbc.gridx = 0; gbc.gridy = 4;
        add(new JLabel("Stock:"), gbc);
        stockField = new JTextField();
        stockField.setPreferredSize(new Dimension(200, 25)); // Set preferred width
        stockField.setText(isUpdate ? String.valueOf(book.getStock()) : "");
        gbc.gridx = 1; gbc.gridy = 4;
        add(stockField, gbc);

        // Price
        gbc.gridx = 0; gbc.gridy = 5;
        add(new JLabel("Price:"), gbc);
        priceField = new JTextField();
        priceField.setPreferredSize(new Dimension(200, 25)); // Set preferred width
        priceField.setText(isUpdate ? String.valueOf(book.getPrice()) : "");
        gbc.gridx = 1; gbc.gridy = 5;
        add(priceField, gbc);

        // Image
        gbc.gridx = 0; gbc.gridy = 6;
        add(new JLabel("Image:"), gbc);
        imageLabel = new JLabel(isUpdate ? book.getImagePath() : "No Image", SwingConstants.CENTER);
        gbc.gridx = 1; gbc.gridy = 6;
        add(imageLabel, gbc);

        // Upload Image Button
        JButton uploadButton = new JButton("Upload Image");
        gbc.gridx = 0; gbc.gridy = 7;
        add(uploadButton, gbc);
        uploadButton.addActionListener(e -> uploadImage());

        // Save Button
        JButton saveButton = new JButton(isUpdate ? "Update" : "Add");
        gbc.gridx = 1; gbc.gridy = 7;
        add(saveButton, gbc);
        saveButton.addActionListener(e -> saveBook());

        setSize(400, 400);
        setLocationRelativeTo(parent); // Center the dialog relative to the parent
        setVisible(true);
    }

    private void uploadImage() {
        JFileChooser fileChooser = new JFileChooser();
        if (fileChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            String imagePath = fileChooser.getSelectedFile().getAbsolutePath();
            imageLabel.setText(imagePath);
        }
    }

    private void saveBook() {
        // Validate fields and create a new Book object
        String title = titleField.getText();
        String author = authorField.getText();
        String publisher = publisherField.getText();
        int year = Integer.parseInt(yearField.getText());
        int stock = Integer.parseInt(stockField.getText());
        double price = Double.parseDouble(priceField.getText());
        String imagePath = imageLabel.getText();

        // Create or update the book object
        if (isUpdate) {
            book.setTitle(title);
            book.setAuthor(author);
            book.setPublisher(publisher);
            book.setYear(year);
            book.setStock(stock);
            book.setPrice(price);
            book.setImagePath(imagePath);
            // Logic to update the book in your data structure can be added here
        } else {
            book = new Book(title, author, publisher, year, stock, imagePath, price);
            // Logic to add the new book to your data structure can be added here
        }

        // Close the dialog after saving
        dispose(); // Close the dialog
    }
}

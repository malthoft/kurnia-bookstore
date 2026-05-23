package org.example.uapproglans3;

import javax.swing.*;
import java.awt.*;


public class BookManagementGUI extends JDialog {
    private JTextField titleField, authorField, publisherField, yearField, stockField, priceField;
    private JLabel imageLabel;
    private Book book;
    private boolean isUpdate;
    private BookStoreGUI parentGUI;

    public BookManagementGUI(BookStoreGUI parent, String title, Book book) {
        super(parent, title, true);
        this.parentGUI = parent;
        this.book = book;
        this.isUpdate = book != null;
        this.parentGUI = parent;

        setLayout(new GridBagLayout());

        // SRP: UI setup didelegasikan ke method spesifik
        initializeFormFields();
        initializeButtons();

        setSize(400, 400);
        setLocationRelativeTo(parent);
        setVisible(true);
    }

    private void initializeFormFields() {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);

        // Title
        titleField = createFormField(gbc, "Title:", 0, isUpdate ? book.getTitle() : "");
        // Author
        authorField = createFormField(gbc, "Author:", 1, isUpdate ? book.getAuthor() : "");
        // Publisher
        publisherField = createFormField(gbc, "Publisher:", 2, isUpdate ? book.getPublisher() : "");
        // Year
        yearField = createFormField(gbc, "Year:", 3, isUpdate ? String.valueOf(book.getYear()) : "");
        // Stock
        stockField = createFormField(gbc, "Stock:", 4, isUpdate ? String.valueOf(book.getStock()) : "");
        // Price
        priceField = createFormField(gbc, "Price:", 5, isUpdate ? String.valueOf(book.getPrice()) : "");

        // Image Label
        gbc.gridx = 0;
        gbc.gridy = 6;
        add(new JLabel("Image:"), gbc);
        imageLabel = new JLabel(isUpdate ? book.getImagePath() : "No Image", SwingConstants.CENTER);
        gbc.gridx = 1;
        gbc.gridy = 6;
        add(imageLabel, gbc);
    }

    /**
     * SRP: Factory method untuk membuat form field.
     * Menghindari duplikasi kode pembuatan label + textfield.
     * OCP: Jika ingin mengubah style field, cukup ubah method ini.
     */
    private JTextField createFormField(GridBagConstraints gbc, String label, int row, String value) {
        gbc.gridx = 0;
        gbc.gridy = row;
        add(new JLabel(label), gbc);
        JTextField field = new JTextField();
        field.setPreferredSize(new Dimension(200, 25));
        field.setText(value);
        gbc.gridx = 1;
        gbc.gridy = row;
        add(field, gbc);
        return field;
    }

    /** SRP: HANYA membuat tombol Upload dan Save/Update. */
    private void initializeButtons() {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);

        JButton uploadButton = new JButton("Upload Image");
        gbc.gridx = 0;
        gbc.gridy = 7;
        add(uploadButton, gbc);
        uploadButton.addActionListener(e -> uploadImage());

        JButton saveButton = new JButton(isUpdate ? "Update" : "Add");
        gbc.gridx = 1;
        gbc.gridy = 7;
        add(saveButton, gbc);
        saveButton.addActionListener(e -> saveBook());
    }

    // ==================== BUSINESS LOGIC (SRP) ====================

    /** SRP: HANYA menangani upload gambar via file chooser. */
    private void uploadImage() {
        JFileChooser fileChooser = new JFileChooser();
        if (fileChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            String imagePath = fileChooser.getSelectedFile().getAbsolutePath();
            imageLabel.setText(imagePath);
        }
    }

    /**
     * SRP: Method saveBook() sekarang mendelegasikan ke:
     * - validateInput(): validasi data (SRP terpisah)
     * - collectBookData(): pengumpulan data dari form (SRP terpisah)
     * - Logika save/update buku
     *
     * SEBELUM: Semua logika ada di satu method tanpa validasi.
     * SESUDAH: Setiap langkah punya method sendiri.
     */
    private void saveBook() {
        // SRP: Validasi dipisahkan dari logika penyimpanan
        if (!validateInput())
            return;

        // SRP: Pengumpulan data dipisahkan dari logika penyimpanan
        String title = titleField.getText().trim();
        String author = authorField.getText().trim();
        String publisher = publisherField.getText().trim();
        int year = Integer.parseInt(yearField.getText().trim());
        int stock = Integer.parseInt(stockField.getText().trim());
        double price = Double.parseDouble(priceField.getText().trim());
        String imagePath = imageLabel.getText();

        if (isUpdate) {
            book.setTitle(title);
            book.setAuthor(author);
            book.setPublisher(publisher);
            book.setYear(year);
            book.setStock(stock);
            book.setPrice(price);
            book.setImagePath(imagePath);

            int index = parentGUI.bookDatabase.getBooks().indexOf(book);
            if (index != -1) {
                parentGUI.updateBook(index, book);
            } else {
                parentGUI.loadBooksToTable();
            }
        } else {
            book = new Book(title, author, publisher, year, stock, imagePath, price);
            parentGUI.addBook(book);
        }

        dispose();
    }

    /**
     * SRP: HANYA memvalidasi input form.
     * Satu alasan berubah: jika aturan validasi berubah.
     *
     * OCP: Bisa di-override untuk aturan validasi yang lebih ketat
     * (misal: tahun harus > 1900, harga harus > 0).
     *
     * @return true jika semua input valid
     */
    protected boolean validateInput() {
        if (titleField.getText().trim().isEmpty()) {
            showErrorMessage("Title tidak boleh kosong!");
            return false;
        }
        if (authorField.getText().trim().isEmpty()) {
            showErrorMessage("Author tidak boleh kosong!");
            return false;
        }
        try {
            Integer.parseInt(yearField.getText().trim());
        } catch (NumberFormatException e) {
            showErrorMessage("Year harus berupa angka!");
            return false;
        }
        try {
            Integer.parseInt(stockField.getText().trim());
        } catch (NumberFormatException e) {
            showErrorMessage("Stock harus berupa angka!");
            return false;
        }
        try {
            Double.parseDouble(priceField.getText().trim());
        } catch (NumberFormatException e) {
            showErrorMessage("Price harus berupa angka!");
            return false;
        }
        return true;
    }

    /** SRP: Utilitas pesan error. */
    private void showErrorMessage(String message) {
        JOptionPane.showMessageDialog(this, message, "Error", JOptionPane.ERROR_MESSAGE);
    }
}

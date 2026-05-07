package org.example.uapproglans3;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class CustomerManagementGUI extends JDialog {
    private JTextField idField, nameField, addressField;
    private JRadioButton maleButton, femaleButton;
    private JTable customerTable;
    private DefaultTableModel tableModel;
    private CustomerDatabase customerDatabase;

    public CustomerManagementGUI(CustomerDatabase customerDatabase) {
        this.customerDatabase = customerDatabase;
        setTitle("Data Pelanggan");
        setSize(600, 400);
        setLayout(new BorderLayout());

        // Form Panel
        JPanel formPanel = new JPanel(new GridLayout(5, 2));
        idField = new JTextField();
        nameField = new JTextField();
        addressField = new JTextField();
        maleButton = new JRadioButton("Laki-Laki");
        femaleButton = new JRadioButton("Perempuan");
        ButtonGroup genderGroup = new ButtonGroup();
        genderGroup.add(maleButton);
        genderGroup.add(femaleButton);

        formPanel.add(new JLabel("Kode Pelanggan:"));
        formPanel.add(idField);
        formPanel.add(new JLabel("Nama:"));
        formPanel.add(nameField);
        formPanel.add(new JLabel("Jenis Kelamin:"));
        JPanel genderPanel = new JPanel();
        genderPanel.add(maleButton);
        genderPanel.add(femaleButton);
        formPanel.add(genderPanel);
        formPanel.add(new JLabel("Alamat:"));
        formPanel.add(addressField);

        add(formPanel, BorderLayout.NORTH);

        // Table
        tableModel = new DefaultTableModel(new String[]{"ID", "Nama", "Gender", "Alamat"}, 0);
        customerTable = new JTable(tableModel);
        loadCustomersToTable();
        add(new JScrollPane(customerTable), BorderLayout.CENTER);

        // Button Panel
        JPanel buttonPanel = new JPanel();
        JButton addButton = new JButton("Tambah");
        JButton updateButton = new JButton("Ubah");
        JButton deleteButton = new JButton("Hapus");
        buttonPanel.add(addButton);
        buttonPanel.add(updateButton);
        buttonPanel.add(deleteButton);
        add(buttonPanel, BorderLayout.SOUTH);

        // Action Listeners
        addButton.addActionListener(e -> addCustomer());
        updateButton.addActionListener(e -> updateCustomer());
        deleteButton.addActionListener(e -> deleteCustomer());

        setVisible(true);
    }

    private void loadCustomersToTable() {
        tableModel.setRowCount(0);
        for (Customer customer : customerDatabase.getCustomers()) {
            tableModel.addRow(new Object[]{customer.getId(), customer.getName(), customer.getGender(), customer.getAddress()});
        }
    }

    private void addCustomer() {
        String id = idField.getText();
        String name = nameField.getText();
        String gender = maleButton.isSelected() ? "L" : "P";
        String address = addressField.getText();

        Customer customer = new Customer(id, name, gender, address);
        customerDatabase.addCustomer(customer);
        loadCustomersToTable();
        clearFields();
    }

    private void updateCustomer() {
        int selectedRow = customerTable.getSelectedRow();
        if (selectedRow != -1) {
            String id = idField.getText();
            String name = nameField.getText();
            String gender = maleButton.isSelected() ? "L" : "P";
            String address = addressField.getText();

            Customer customer = new Customer(id, name, gender, address);
            customerDatabase.updateCustomer(selectedRow, customer);
            loadCustomersToTable();
            clearFields();
        } else {
            JOptionPane.showMessageDialog(this, "Pilih pelanggan untuk diubah!", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void deleteCustomer() {
        int selectedRow = customerTable.getSelectedRow();
        if (selectedRow != -1) {
            customerDatabase.deleteCustomer(selectedRow);
            loadCustomersToTable();
        } else {
            JOptionPane.showMessageDialog(this, "Pilih pelanggan untuk dihapus!", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void clearFields() {
        idField.setText("");
        nameField.setText("");
        addressField.setText("");
        maleButton.setSelected(false);
        femaleButton.setSelected(false);
    }
}


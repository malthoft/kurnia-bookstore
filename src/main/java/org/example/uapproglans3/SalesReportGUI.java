package org.example.uapproglans3;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class SalesReportGUI extends JDialog {
    private BookStoreGUI parent;
    private JTable reportTable;
    private DefaultTableModel tableModel;

    public SalesReportGUI(BookStoreGUI parent) {
        super(parent, "Laporan Penjualan", true);
        this.parent = parent;

        setLayout(new BorderLayout());
        setSize(600, 400);

        // Tabel untuk laporan penjualan
        tableModel = new DefaultTableModel(new String[]{"Order ID", "Transaction ID", "Customer ID", "Book Title", "Quantity", "Subtotal"}, 0);
        reportTable = new JTable(tableModel);
        loadSalesReport();
        add(new JScrollPane(reportTable), BorderLayout.CENTER);

        // Button Panel
        JPanel buttonPanel = new JPanel();
        JButton closeButton = new JButton("Tutup");
        buttonPanel.add(closeButton);
        add(buttonPanel, BorderLayout.SOUTH);

        closeButton.addActionListener(e -> dispose());

        setVisible(true);
    }

    private void loadSalesReport() {
        for (Transaction transaction : parent.getTransactionDatabase().getTransactions()) {
            tableModel.addRow(new Object[]{
                    transaction.getOrderId(),
                    transaction.getTransactionId(),
                    transaction.getCustomerId(),
                    transaction.getBookTitle(), // Mengambil judul buku dari transaksi
                    transaction.getQuantity(),
                    transaction.getSubtotal()
            });
        }
    }
}

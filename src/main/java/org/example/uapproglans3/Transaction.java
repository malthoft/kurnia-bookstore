package org.example.uapproglans3;

import java.io.Serializable;

public class Transaction implements Serializable {
    private String orderId;
    private String transactionId;
    private String customerId; // Menyimpan ID pelanggan
    private String bookTitle; // Menyimpan judul buku
    private int quantity;
    private double subtotal;

    public Transaction(String orderId, String transactionId, String customerId, String bookTitle, int quantity, double subtotal) {
        this.orderId = orderId;
        this.transactionId = transactionId;
        this.customerId = customerId; // Simpan ID pelanggan
        this.bookTitle = bookTitle; // Simpan judul buku
        this.quantity = quantity;
        this.subtotal = subtotal;
    }

    // Getters
    public String getOrderId() {
        return orderId;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public String getCustomerId() {
        return customerId; // Tambahkan getter untuk ID pelanggan
    }

    public String getBookTitle() {
        return bookTitle; // Tambahkan getter untuk judul buku
    }

    public int getQuantity() {
        return quantity;
    }

    public double getSubtotal() {
        return subtotal;
    }
}

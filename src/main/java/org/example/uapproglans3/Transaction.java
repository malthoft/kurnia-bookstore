package org.example.uapproglans3;

import java.io.Serializable;

// OCP: Entitas terbuka untuk perluasan (bisa tambah jenis diskon baru)
// tapi tertutup untuk modifikasi (tidak perlu mengubah kelas Transaction).
interface IDiscountStrategy extends Serializable {
    double applyDiscount(double subtotal);
}

// Strategi default (tanpa diskon)
class NoDiscountStrategy implements IDiscountStrategy {
    @Override
    public double applyDiscount(double subtotal) {
        return subtotal;
    }
}

public class Transaction implements Serializable {
    private String orderId;
    private String transactionId;
    private String customerId; // Menyimpan ID pelanggan
    private String bookTitle; // Menyimpan judul buku
    private int quantity;
    private double subtotal;
    
    // Menggunakan abstraksi (IDiscountStrategy) agar bisa diperluas
    private IDiscountStrategy discountStrategy;

    public Transaction(String orderId, String transactionId, String customerId, String bookTitle, int quantity, double subtotal) {
        this.orderId = orderId;
        this.transactionId = transactionId;
        this.customerId = customerId; // Simpan ID pelanggan
        this.bookTitle = bookTitle; // Simpan judul buku
        this.quantity = quantity;
        this.subtotal = subtotal;
        this.discountStrategy = new NoDiscountStrategy(); // Default
    }

    // Metode untuk menyuntikkan strategi diskon yang berbeda tanpa mengubah kode Transaction
    public void setDiscountStrategy(IDiscountStrategy discountStrategy) {
        this.discountStrategy = discountStrategy;
    }

    // Mengambil total harga setelah diskon diterapkan
    public double getFinalTotal() {
        return discountStrategy.applyDiscount(this.subtotal);
    }

    // Getters
    public String getOrderId() {
        return orderId;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public String getCustomerId() {
        return customerId; 
    }

    public String getBookTitle() {
        return bookTitle; 
    }

    public int getQuantity() {
        return quantity;
    }

    public double getSubtotal() {
        return subtotal;
    }
}

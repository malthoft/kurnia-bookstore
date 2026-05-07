package org.example.uapproglans3;

import java.io.Serializable;

public class CartItem implements Serializable {
    private String bookCode;
    private String bookTitle;
    private int quantity;
    private double price;

    public CartItem(String bookCode, String bookTitle, int quantity, double price) {
        this.bookCode = bookCode;
        this.bookTitle = bookTitle;
        this.quantity = quantity;
        this.price = price;
    }

    // Getters
    public String getBookCode() {
        return bookCode;
    }

    public String getBookTitle() {
        return bookTitle;
    }

    public int getQuantity() {
        return quantity;
    }

    public double getPrice() {
        return price;
    }

    public double getSubtotal() {
        return quantity * price;
    }
}

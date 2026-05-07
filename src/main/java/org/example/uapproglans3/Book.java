package org.example.uapproglans3;

import java.io.Serializable;

public class Book implements Serializable {
    private String title;
    private String author;
    private String publisher;
    private int year;
    private int stock;
    private String imagePath;
    private double price;

    public Book(String title, String author, String publisher, int year, int stock, String imagePath, double price) {
        this.title = title;
        this.author = author;
        this.publisher = publisher;
        this.year = year;
        this.stock = stock;
        this.imagePath = imagePath;
        this.price = price;
    }

    // Getters and Setters
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }
}

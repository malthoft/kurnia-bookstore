package org.example.uapproglans3;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class BookDatabase {
    private List<Book> books;
    private final String filePath = "books.dat";

    public BookDatabase() {
        books = new ArrayList<>();
        loadBooks();
    }

    public void addBook(Book book) {
        books.add(book);
        saveBooks();
    }

    public List<Book> getBooks() {
        return books;
    }

    public void updateBook(int index, Book book) {
        books.set(index, book);
        saveBooks();
    }

    public void deleteBook(int index) {
        books.remove(index);
        saveBooks();
    }

    private void saveBooks() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(filePath))) {
            oos.writeObject(books);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadBooks() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(filePath))) {
            books = (List<Book>) ois.readObject();
        } catch (FileNotFoundException e) {
            // File not found, starting with an empty list
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}

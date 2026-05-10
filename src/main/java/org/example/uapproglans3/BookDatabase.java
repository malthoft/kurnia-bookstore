package org.example.uapproglans3;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

// 1. Interface BookRepository sebagai jembatan abstraksi (Memenuhi DIP dan OCP)
interface BookRepository {
    void saveBooks(List<Book> books);
    List<Book> loadBooks();
}

// 2. Class FileBookRepository khusus mengurus simpan/memuat dari file (Memenuhi SRP)
// Jika besok ingin pakai MySQL, tinggal buat class MySQLBookRepository tanpa ubah logika bisnis.
class FileBookRepository implements BookRepository {
    private final String filePath;

    public FileBookRepository(String filePath) {
        this.filePath = filePath;
    }

    @Override
    public void saveBooks(List<Book> books) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(filePath))) {
            oos.writeObject(books);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<Book> loadBooks() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(filePath))) {
            return (List<Book>) ois.readObject();
        } catch (FileNotFoundException e) {
            // File tidak ditemukan, kembalikan list kosong
            return new ArrayList<>();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }
}

// 3. Class BookService (sebelumnya BookDatabase), bertindak sebagai Manajer
// Hanya fokus mengurus logika bisnis daftar buku (Memenuhi SRP)
class BookService {
    private List<Book> books;
    private final BookRepository repository;

    // Bergantung pada abstraksi interface (BookRepository), bukan implementasi spesifik (Memenuhi DIP)
    public BookService(BookRepository repository) {
        this.repository = repository;
        this.books = repository.loadBooks();
    }

    public void addBook(Book book) {
        books.add(book);
        repository.saveBooks(books);
    }

    public List<Book> getBooks() {
        return books;
    }

    public void updateBook(int index, Book book) {
        books.set(index, book);
        repository.saveBooks(books);
    }

    public void deleteBook(int index) {
        books.remove(index);
        repository.saveBooks(books);
    }
}

// Class public BookDatabase tetap dipertahankan sebagai wrapper (pembungkus)
// Agar nama file 'BookDatabase.java' tetap valid dan kode lain (misal GUI/Main) yang menggunakan
// kelas ini tidak error karena perubahan nama kelas menjadi BookService.
public class BookDatabase {
    private final BookService service;

    public BookDatabase() {
        // Injeksi dependensi: menggunakan FileBookRepository untuk memuat dari "books.dat"
        BookRepository repo = new FileBookRepository("books.dat");
        this.service = new BookService(repo);
    }

    public void addBook(Book book) {
        service.addBook(book);
    }

    public List<Book> getBooks() {
        return service.getBooks();
    }

    public void updateBook(int index, Book book) {
        service.updateBook(index, book);
    }

    public void deleteBook(int index) {
        service.deleteBook(index);
    }
}

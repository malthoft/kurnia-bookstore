package org.example.uapproglans3;

import java.util.ArrayList;
import java.util.List;

public class FakeBookRepository implements BookRepository {
    private List<Book> storage;

    public FakeBookRepository() {
        this.storage = new ArrayList<>();
    }

    // Constructor untuk inisialisasi dengan data awal jika diperlukan
    public FakeBookRepository(List<Book> initialBooks) {
        this.storage = new ArrayList<>(initialBooks);
    }

    @Override
    public void saveBooks(List<Book> books) {
        // Pada Fake, kita cukup mengupdate reference storage kita di memory.
        this.storage = new ArrayList<>(books);
    }

    @Override
    public List<Book> loadBooks() {
        // Mengembalikan salinan dari storage agar mirip dengan membaca dari file
        return new ArrayList<>(storage);
    }
}

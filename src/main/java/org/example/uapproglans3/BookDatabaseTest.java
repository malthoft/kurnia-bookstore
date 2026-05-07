package org.example.uapproglans3;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class BookDatabaseTest {
    private BookDatabase bookDatabase;

    @BeforeEach
    public void setUp() {
        bookDatabase = new BookDatabase();
    }

    @Test
    public void testAddBook() {
        Book book = new Book("Harry Potter", "JK Rowling", "Gramed", 2000, 10, "", 100000);
        bookDatabase.addBook(book);

        List<Book> books = bookDatabase.getBooks();
        assertEquals(1, books.size());
        assertEquals("Harry Potter", books.get(0).getTitle());
    }

}

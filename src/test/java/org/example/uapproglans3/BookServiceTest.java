package org.example.uapproglans3;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class BookServiceTest {

    private BookService bookService;
    private FakeBookRepository fakeRepository;

    @BeforeEach
    void setUp() {
        // Inisialisasi Test Double bertipe Fake
        fakeRepository = new FakeBookRepository();
        
        // Inject fakeRepository ke dalam BookService
        bookService = new BookService(fakeRepository);
    }

    @Test
    void testAddBook() {
        // Arrange
        Book newBook = new Book("Buku Test", "Penulis Test", "Penerbit Test", 2024, 10, "path/to/image.png", 50000.0);

        // Act
        bookService.addBook(newBook);

        // Assert
        List<Book> books = bookService.getBooks();
        assertEquals(1, books.size(), "Jumlah buku seharusnya 1 setelah penambahan.");
        assertEquals("Buku Test", books.get(0).getTitle());
        
        // Pastikan repositori ikut tersimpan (walaupun hanya in-memory)
        List<Book> savedInRepo = fakeRepository.loadBooks();
        assertEquals(1, savedInRepo.size());
    }

    @Test
    void testGetBooksWhenEmpty() {
        // Act
        List<Book> books = bookService.getBooks();

        // Assert
        assertTrue(books.isEmpty(), "List buku seharusnya kosong pada awalnya.");
    }

    @Test
    void testUpdateBook() {
        // Arrange
        Book oldBook = new Book("Buku Lama", "Penulis Lama", "Penerbit Lama", 2020, 5, "img.png", 30000.0);
        bookService.addBook(oldBook);
        
        Book updatedBook = new Book("Buku Baru", "Penulis Baru", "Penerbit Baru", 2021, 15, "new_img.png", 45000.0);

        // Act
        bookService.updateBook(0, updatedBook);

        // Assert
        List<Book> books = bookService.getBooks();
        assertEquals(1, books.size());
        assertEquals("Buku Baru", books.get(0).getTitle());
        assertEquals(45000.0, books.get(0).getPrice());
        
        // Pastikan perubahan tercermin di repositori
        assertEquals("Buku Baru", fakeRepository.loadBooks().get(0).getTitle());
    }

    @Test
    void testDeleteBook() {
        // Arrange
        Book book1 = new Book("Buku Satu", "Penulis Satu", "Penerbit", 2020, 5, "img1.png", 30000.0);
        Book book2 = new Book("Buku Dua", "Penulis Dua", "Penerbit", 2021, 10, "img2.png", 40000.0);
        bookService.addBook(book1);
        bookService.addBook(book2);

        // Act
        bookService.deleteBook(0); // Menghapus Buku Satu

        // Assert
        List<Book> books = bookService.getBooks();
        assertEquals(1, books.size(), "Jumlah buku seharusnya 1 setelah penghapusan.");
        assertEquals("Buku Dua", books.get(0).getTitle(), "Buku Dua seharusnya masih ada di index 0.");
        
        // Pastikan repositori juga hanya berisi 1 buku
        assertEquals(1, fakeRepository.loadBooks().size());
    }
}

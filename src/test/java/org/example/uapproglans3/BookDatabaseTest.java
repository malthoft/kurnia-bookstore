package org.example.uapproglans3;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class BookDatabaseTest {

    static class FakeBookRepository implements BookRepository {
        private List<Book> savedBooks = new ArrayList<>();

        @Override
        public void saveBooks(List<Book> books) {
            savedBooks = new ArrayList<>(books);
        }

        @Override
        public List<Book> loadBooks() {
            return new ArrayList<>(savedBooks);
        }
    }

    @Nested
    class BookServiceTest {
        private FakeBookRepository fakeRepo;
        private BookService service;

        @BeforeEach
        void setUp() {
            fakeRepo = new FakeBookRepository();
            service = new BookService(fakeRepo);
        }

        @Test
        void testAddBook() {
            Book book = new Book("Harry Potter", "JK Rowling", "Gramedia", 2000, 10, "", 100000);

            service.addBook(book);

            List<Book> books = service.getBooks();
            assertEquals(1, books.size());
            assertEquals("Harry Potter", books.get(0).getTitle());
        }

        @Test
        void testAddMultipleBooks() {
            service.addBook(new Book("Book A", "Author A", "Pub A", 2020, 5, "", 50000));
            service.addBook(new Book("Book B", "Author B", "Pub B", 2021, 3, "", 60000));
            service.addBook(new Book("Book C", "Author C", "Pub C", 2022, 7, "", 70000));

            assertEquals(3, service.getBooks().size());
        }

        @Test
        void testGetBooksInitiallyEmpty() {
            List<Book> books = service.getBooks();

            assertTrue(books.isEmpty());
        }

        @Test
        void testUpdateBook() {
            Book book = new Book("Old Title", "Author", "Publisher", 2020, 5, "", 50000);
            service.addBook(book);

            Book updatedBook = new Book("New Title", "New Author", "New Publisher", 2023, 10, "", 75000);
            service.updateBook(0, updatedBook);

            assertEquals("New Title", service.getBooks().get(0).getTitle());
            assertEquals("New Author", service.getBooks().get(0).getAuthor());
        }

        @Test
        void testDeleteBook() {
            service.addBook(new Book("Book A", "Author A", "Pub A", 2020, 5, "", 50000));
            service.addBook(new Book("Book B", "Author B", "Pub B", 2021, 3, "", 60000));

            service.deleteBook(0);

            assertEquals(1, service.getBooks().size());
            assertEquals("Book B", service.getBooks().get(0).getTitle());
        }

        @Test
        void testFakePersistence() {
            FakeBookRepository sharedFake = new FakeBookRepository();
            BookService serviceA = new BookService(sharedFake);
            serviceA.addBook(new Book("Persistent Book", "Author", "Pub", 2020, 5, "", 50000));

            BookService serviceB = new BookService(sharedFake);

            assertEquals(1, serviceB.getBooks().size());
            assertEquals("Persistent Book", serviceB.getBooks().get(0).getTitle());
        }
    }

    @Nested
    class FileBookRepositoryTest {
        private File tempFile;
        private FileBookRepository repository;

        @BeforeEach
        void setUp() throws Exception {
            tempFile = File.createTempFile("test_books", ".dat");
            tempFile.deleteOnExit();
            repository = new FileBookRepository(tempFile.getAbsolutePath());
        }

        @AfterEach
        void tearDown() {
            tempFile.delete();
        }

        @Test
        void testSaveAndLoadBooks() {
            List<Book> books = new ArrayList<>();
            books.add(new Book("Book X", "Author X", "Pub X", 2020, 5, "", 50000));
            books.add(new Book("Book Y", "Author Y", "Pub Y", 2021, 3, "", 60000));

            repository.saveBooks(books);
            List<Book> loaded = repository.loadBooks();

            assertEquals(2, loaded.size());
            assertEquals("Book X", loaded.get(0).getTitle());
            assertEquals("Book Y", loaded.get(1).getTitle());
        }

        @Test
        void testLoadBooksFileNotFound() {
            FileBookRepository emptyRepo = new FileBookRepository("file_yang_tidak_ada.dat");

            List<Book> books = emptyRepo.loadBooks();

            assertNotNull(books);
            assertTrue(books.isEmpty());
        }
    }
}

package org.example.uapproglans3;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class TransactionServiceTest {

    private TransactionService transactionService;
    private FakeTransactionRepository fakeRepository;

    @BeforeEach
    void setUp() {
        // Inisialisasi Fake Repository
        fakeRepository = new FakeTransactionRepository();
        
        // Inject ke TransactionService
        transactionService = new TransactionService(fakeRepository);
    }

    @Test
    void testAddTransaction() {
        // Arrange
        Transaction newTransaction = new Transaction("ORD123", "TRX001", "CUST001", "Buku Test", 2, 100000.0);

        // Act
        transactionService.addTransaction(newTransaction);

        // Assert
        List<Transaction> transactions = transactionService.getTransactions();
        assertEquals(1, transactions.size(), "Jumlah transaksi seharusnya 1 setelah penambahan.");
        assertEquals("ORD123", transactions.get(0).getOrderId());
        assertEquals("TRX001", transactions.get(0).getTransactionId());
        
        // Pastikan repositori ikut tersimpan (in-memory)
        List<Transaction> savedInRepo = fakeRepository.loadTransactions();
        assertEquals(1, savedInRepo.size());
    }

    @Test
    void testGetTransactionsWhenEmpty() {
        // Act
        List<Transaction> transactions = transactionService.getTransactions();

        // Assert
        assertTrue(transactions.isEmpty(), "List transaksi seharusnya kosong pada awalnya.");
    }
}

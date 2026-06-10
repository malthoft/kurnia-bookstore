package org.example.uapproglans3;

import java.util.ArrayList;
import java.util.List;

public class FakeTransactionRepository implements TransactionRepository {
    private List<Transaction> storage;

    public FakeTransactionRepository() {
        this.storage = new ArrayList<>();
    }

    public FakeTransactionRepository(List<Transaction> initialTransactions) {
        this.storage = new ArrayList<>(initialTransactions);
    }

    @Override
    public void saveTransactions(List<Transaction> transactions) {
        this.storage = new ArrayList<>(transactions);
    }

    @Override
    public List<Transaction> loadTransactions() {
        return new ArrayList<>(storage);
    }
}

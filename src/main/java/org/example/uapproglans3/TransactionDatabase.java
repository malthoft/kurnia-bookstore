package org.example.uapproglans3;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

interface TransactionRepository {
    void saveTransactions(List<Transaction> transactions);
    List<Transaction> loadTransactions();
}

class FileTransactionRepository implements TransactionRepository {
    private final String filePath;

    public FileTransactionRepository(String filePath) {
        this.filePath = filePath;
    }

    @Override
    public void saveTransactions(List<Transaction> transactions) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(filePath))) {
            oos.writeObject(transactions);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<Transaction> loadTransactions() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(filePath))) {
            return (List<Transaction>) ois.readObject();
        } catch (FileNotFoundException e) {
            // File not found, starting with an empty list
            return new ArrayList<>();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }
}

class TransactionService {
    private List<Transaction> transactions;
    private final TransactionRepository repository;

    public TransactionService(TransactionRepository repository) {
        this.repository = repository;
        this.transactions = repository.loadTransactions();
    }

    public void addTransaction(Transaction transaction) {
        transactions.add(transaction);
        repository.saveTransactions(transactions);
    }

    public List<Transaction> getTransactions() {
        return transactions;
    }
}

public class TransactionDatabase {
    private final TransactionService service;

    public TransactionDatabase() {
        TransactionRepository repo = new FileTransactionRepository("transactions.dat");
        this.service = new TransactionService(repo);
    }

    public void addTransaction(Transaction transaction) {
        service.addTransaction(transaction);
    }

    public List<Transaction> getTransactions() {
        return service.getTransactions();
    }
}

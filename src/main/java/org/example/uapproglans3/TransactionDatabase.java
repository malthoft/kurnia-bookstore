package org.example.uapproglans3;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class TransactionDatabase {
    private List<Transaction> transactions;
    private final String filePath = "transactions.dat";

    public TransactionDatabase() {
        transactions = new ArrayList<>();
        loadTransactions();
    }

    public void addTransaction(Transaction transaction) {
        transactions.add(transaction);
        saveTransactions(); // Pastikan data disimpan setelah ditambahkan
    }

    public List<Transaction> getTransactions() {
        return transactions;
    }

    private void saveTransactions() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(filePath))) {
            oos.writeObject(transactions);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadTransactions() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(filePath))) {
            transactions = (List<Transaction>) ois.readObject();
        } catch (FileNotFoundException e) {
            // File not found, starting with an empty list
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}



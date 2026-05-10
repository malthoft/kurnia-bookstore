package org.example.uapproglans3;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

// SRP: Memisahkan tanggung jawab pengelolaan file (I/O) ke kelas terpisah
class CustomerFileManager {
    private final String filePath = "customers.dat";

    public void saveCustomers(List<Customer> customers) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(filePath))) {
            oos.writeObject(customers);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @SuppressWarnings("unchecked")
    public List<Customer> loadCustomers() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(filePath))) {
            return (List<Customer>) ois.readObject();
        } catch (FileNotFoundException e) {
            // File not found, starting with an empty list
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }
}

public class CustomerDatabase {
    private List<Customer> customers;
    private final CustomerFileManager fileManager;

    public CustomerDatabase() {
        this.fileManager = new CustomerFileManager();
        this.customers = fileManager.loadCustomers();
    }

    public void addCustomer(Customer customer) {
        customers.add(customer);
        fileManager.saveCustomers(customers);
    }

    public List<Customer> getCustomers() {
        return customers;
    }

    public void updateCustomer(int index, Customer customer) {
        customers.set(index, customer);
        fileManager.saveCustomers(customers);
    }

    public void deleteCustomer(int index) {
        customers.remove(index);
        fileManager.saveCustomers(customers);
    }
}

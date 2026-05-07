package org.example.uapproglans3;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class CustomerDatabase {
    private List<Customer> customers;
    private final String filePath = "customers.dat";

    public CustomerDatabase() {
        customers = new ArrayList<>();
        loadCustomers();
    }

    public void addCustomer(Customer customer) {
        customers.add(customer);
        saveCustomers();
    }

    public List<Customer> getCustomers() {
        return customers;
    }

    public void updateCustomer(int index, Customer customer) {
        customers.set(index, customer);
        saveCustomers();
    }

    public void deleteCustomer(int index) {
        customers.remove(index);
        saveCustomers();
    }

    private void saveCustomers() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(filePath))) {
            oos.writeObject(customers);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadCustomers() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(filePath))) {
            customers = (List<Customer>) ois.readObject();
        } catch (FileNotFoundException e) {
            // File not found, starting with an empty list
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}

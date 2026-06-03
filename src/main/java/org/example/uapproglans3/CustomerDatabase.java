package org.example.uapproglans3;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

interface CustomerRepository {
    void saveCustomers(List<Customer> customers);

    List<Customer> loadCustomers();
}

// SRP: Memisahkan tanggung jawab pengelolaan file (I/O) ke kelas terpisah
class FileCustomerRepository implements CustomerRepository {
    private final String filePath;

    public FileCustomerRepository(String filePath) {
        this.filePath = filePath;
    }

    @Override
    public void saveCustomers(List<Customer> customers) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(filePath))) {
            oos.writeObject(customers);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
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

class CustomerService {
    private List<Customer> customers;
    private final CustomerRepository repository;

    public CustomerService(CustomerRepository repository) {
        this.repository = repository;
        this.customers = repository.loadCustomers();
    }

    public void addCustomer(Customer customer) {
        customers.add(customer);
        repository.saveCustomers(customers);
    }

    public List<Customer> getCustomers() {
        return customers;
    }

    public void updateCustomer(int index, Customer customer) {
        customers.set(index, customer);
        repository.saveCustomers(customers);
    }

    public void deleteCustomer(int index) {
        customers.remove(index);
        repository.saveCustomers(customers);
    }
}

public class CustomerDatabase {
    private final CustomerService service;

    public CustomerDatabase() {
        CustomerRepository repo = new FileCustomerRepository("customers.dat");
        this.service = new CustomerService(repo);
    }

    public void addCustomer(Customer customer) {
        service.addCustomer(customer);
    }

    public List<Customer> getCustomers() {
        return service.getCustomers();
    }

    public void updateCustomer(int index, Customer customer) {
        service.updateCustomer(index, customer);
    }

    public void deleteCustomer(int index) {
        service.deleteCustomer(index);
    }
}

package org.example.uapproglans3;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class CustomerDatabaseTest {

    static class SpyCustomerRepository implements CustomerRepository {
        public int saveCustomersCallCount = 0;
        public int loadCustomersCallCount = 0;
        public List<Customer> savedCustomers = new ArrayList<>();
        private List<Customer> initialCustomers;

        public SpyCustomerRepository(List<Customer> initialCustomers) {
            this.initialCustomers = new ArrayList<>(initialCustomers);
            this.savedCustomers = new ArrayList<>(initialCustomers);
        }

        @Override
        public void saveCustomers(List<Customer> customers) {
            saveCustomersCallCount++;
            savedCustomers = new ArrayList<>(customers);
        }

        @Override
        public List<Customer> loadCustomers() {
            loadCustomersCallCount++;
            return new ArrayList<>(initialCustomers);
        }
    }

    @Nested
    class CustomerServiceTest {
        private SpyCustomerRepository spyRepo;
        private CustomerService service;

        @BeforeEach
        void setUp() {
            spyRepo = new SpyCustomerRepository(new ArrayList<>());
            service = new CustomerService(spyRepo);
        }

        @Test
        void testLoadCustomersCalledOnInitialization() {
            assertEquals(1, spyRepo.loadCustomersCallCount);
        }

        @Test
        void testAddCustomerAndSaveCalled() {
            Customer c = new Customer("C01", "Budi", "L", "Jakarta");
            
            service.addCustomer(c);

            assertEquals(1, spyRepo.saveCustomersCallCount);
            assertEquals(1, spyRepo.savedCustomers.size());
            assertEquals("Budi", spyRepo.savedCustomers.get(0).getName());
        }

        @Test
        void testUpdateCustomer() {
            service.addCustomer(new Customer("C01", "Budi", "L", "Jakarta"));
            
            Customer updated = new Customer("C01", "Budi Santoso", "L", "Bandung");
            service.updateCustomer(0, updated);

            assertEquals(2, spyRepo.saveCustomersCallCount);
            assertEquals("Budi Santoso", spyRepo.savedCustomers.get(0).getName());
            assertEquals("Bandung", spyRepo.savedCustomers.get(0).getAddress());
        }

        @Test
        void testDeleteCustomer() {
            service.addCustomer(new Customer("C01", "Budi", "L", "Jakarta"));
            service.addCustomer(new Customer("C02", "Andi", "L", "Surabaya"));

            service.deleteCustomer(0);

            assertEquals(3, spyRepo.saveCustomersCallCount); 
            assertEquals(1, spyRepo.savedCustomers.size());
            assertEquals("Andi", spyRepo.savedCustomers.get(0).getName());
        }
    }

    @Nested
    class FileCustomerRepositoryTest {
        private File tempFile;
        private FileCustomerRepository repository;

        @BeforeEach
        void setUp() throws Exception {
            tempFile = File.createTempFile("test_customers", ".dat");
            tempFile.deleteOnExit();
            repository = new FileCustomerRepository(tempFile.getAbsolutePath());
        }

        @AfterEach
        void tearDown() {
            if (tempFile.exists()) {
                tempFile.delete();
            }
        }

        @Test
        void testSaveAndLoadCustomers() {
            List<Customer> customers = new ArrayList<>();
            customers.add(new Customer("C01", "Siti", "P", "Malang"));
            customers.add(new Customer("C02", "Rudi", "L", "Blitar"));

            repository.saveCustomers(customers);
            List<Customer> loaded = repository.loadCustomers();

            assertEquals(2, loaded.size());
            assertEquals("Siti", loaded.get(0).getName());
            assertEquals("Rudi", loaded.get(1).getName());
        }
    }
}

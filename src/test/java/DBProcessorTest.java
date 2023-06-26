import hw.DB.DBProcessor;
import hw.Shop.Product;
import org.junit.jupiter.api.*;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class DBProcessorTest {
    private DBProcessor dbProcessor;

    @BeforeEach
    void setUp() {
        dbProcessor = new DBProcessor();
    }

    @AfterEach
    void tearDown() {
        dbProcessor.Clear();
    }

    @Test
    void testCreate() {
        Product product = new Product(1, "Milk", 14.88, "Cow Milk");
        dbProcessor.Create(product);

        ArrayList<Product> products = dbProcessor.Read();
        assertEquals(1, products.size());

        Product retrievedProduct = products.get(0);
        assertEquals(product.getTitle(), retrievedProduct.getTitle());
        assertEquals(product.getPrice(), retrievedProduct.getPrice());
        assertEquals(product.getAmount(), retrievedProduct.getAmount());
        assertEquals(product.getCategory(), retrievedProduct.getCategory());
    }

    @Test
    void testRead() {
        Product product1 = new Product(1, "Milk", 14.88, "Cow Milk");
        Product product2 = new Product(2, "Cook", 156.88, "Cum Milk");
        dbProcessor.Create(product1);
        dbProcessor.Create(product2);

        ArrayList<Product> products = dbProcessor.Read();
        assertEquals(2, products.size());

        Product retrievedProduct1 = products.get(0);
        assertEquals(product1.getTitle(), retrievedProduct1.getTitle());
        assertEquals(product1.getPrice(), retrievedProduct1.getPrice());
        assertEquals(product1.getAmount(), retrievedProduct1.getAmount());
        assertEquals(product1.getCategory(), retrievedProduct1.getCategory());

        Product retrievedProduct2 = products.get(1);
        assertEquals(product2.getTitle(), retrievedProduct2.getTitle());
        assertEquals(product2.getPrice(), retrievedProduct2.getPrice());
        assertEquals(product2.getAmount(), retrievedProduct2.getAmount());
        assertEquals(product2.getCategory(), retrievedProduct2.getCategory());
    }

    @Test
    void testUpdate() {
        Product product = new Product(1, "Milk", 14.88, "Cow Milk");
        dbProcessor.Create(product);

        product.setTitle("Updated Milk");
        product.setPrice(20.99);
        product.setAmount(5);
        product.setCategory("Updated Category");
        dbProcessor.Update(product);

        ArrayList<Product> products = dbProcessor.Read();
        assertEquals(1, products.size());

        Product updatedProduct = products.get(0);
        assertEquals(product.getTitle(), updatedProduct.getTitle());
        assertEquals(product.getPrice(), updatedProduct.getPrice());
        assertEquals(product.getAmount(), updatedProduct.getAmount());
        assertEquals(product.getCategory(), updatedProduct.getCategory());
    }

    @Test
    void testDelete() {
        Product product = new Product(1, "Milk", 14.88, "Cow Milk");
        dbProcessor.Create(product);

        dbProcessor.Delete(product.getId());

        ArrayList<Product> products = dbProcessor.Read();
        assertEquals(0, products.size());
    }

    @Test
    void testListByCriteria() {
        Product product1 = new Product(1, "Milk", 14.88, "Cow Milk");
        Product product2 = new Product(2, "Bread", 5.99, "Bakery");
        Product product3 = new Product(3, "Apple", 2.99, "Fruits");
        dbProcessor.Create(product1);
        dbProcessor.Create(product2);
        dbProcessor.Create(product3);

        ArrayList<Product> filteredProducts = dbProcessor.ListByCriteria("PRICE <= 10");
        assertEquals(2, filteredProducts.size());

        // Assert that the filtered products have prices less than or equal to 10
        for (Product product : filteredProducts) {
            assertTrue(product.getPrice() <= 10);
        }
    }
}

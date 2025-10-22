package product;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;

class CatalogTest {
    private Catalog catalog;
    private Product product;

    @BeforeEach
    void setUp() {
        catalog = new Catalog();
        product = new Product("SKU123", "Test Product", 19.99);
    }

    @Test
    void testAddAndFindProduct() {
        catalog.addProduct(product);
        Product found = catalog.findProduct("SKU123");
        assertEquals(product, found);
    }

    @Test
    void testFindMissingProductReturnsNull() {
        assertNull(catalog.findProduct("NONEXISTENT"));
    }

    @Test
    void testContainsProduct() {
        catalog.addProduct(product);
        assertTrue(catalog.containsProduct("SKU123"));
        assertFalse(catalog.containsProduct("NONEXISTENT"));
    }
}
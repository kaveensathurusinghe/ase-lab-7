package product;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class ProductTest {
    @Test
    void testCreateProductWithValidData() {
        Product product = new Product("SKU123", "Test Product", 19.99);
        assertEquals("SKU123", product.getSku());
        assertEquals("Test Product", product.getName());
        assertEquals(19.99, product.getPrice(), 0.001);
    }

    @Test
    void testCreateProductFailsWithNegativePrice() {
        assertThrows(IllegalArgumentException.class, () -> {
            new Product("SKU123", "Test Product", -5.0);
        });
    }

    @Test
    void testCreateProductFailsWithEmptySku() {
        assertThrows(IllegalArgumentException.class, () -> {
            new Product("", "Test Product", 19.99);
        });
    }
}
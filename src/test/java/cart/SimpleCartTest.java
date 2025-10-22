package cart;

import product.Product;
import product.Catalog;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;

class SimpleCartTest {
    private Catalog catalog;
    private SimpleCart cart;
    private Product testProduct;

    @BeforeEach
    void setUp() {
        catalog = new Catalog();
        cart = new SimpleCart(catalog);
        testProduct = new Product("SKU123", "Test Product", 19.99);
        catalog.addProduct(testProduct);
    }

    @Test
    void testAddItemToCart() {
        cart.addItem("SKU123", 2);
        assertEquals(39.98, cart.getTotal(), 0.001);
    }

    @Test
    void testAddItemFailsForMissingProduct() {
        assertThrows(IllegalArgumentException.class, () -> {
            cart.addItem("NONEXISTENT", 1);
        });
    }

    @Test
    void testAddItemFailsForZeroQuantity() {
        assertThrows(IllegalArgumentException.class, () -> {
            cart.addItem("SKU123", 0);
        });
    }

    @Test
    void testAddItemFailsForNegativeQuantity() {
        assertThrows(IllegalArgumentException.class, () -> {
            cart.addItem("SKU123", -1);
        });
    }

    @Test
    void testRemoveItem() {
        cart.addItem("SKU123", 2);
        cart.removeItem("SKU123");
        assertEquals(0.0, cart.getTotal(), 0.001);
    }

    @Test
    void testTotalCalculationWithMultipleItems() {
        Product product2 = new Product("SKU456", "Another Product", 25.50);
        catalog.addProduct(product2);
        
        cart.addItem("SKU123", 2); // 2 * 19.99 = 39.98
        cart.addItem("SKU456", 1); // 1 * 25.50 = 25.50
        
        assertEquals(65.48, cart.getTotal(), 0.001);
    }

    @Test
    void testAddSameItemTwiceIncreasesQuantity() {
        cart.addItem("SKU123", 2);
        cart.addItem("SKU123", 1);
        assertEquals(59.97, cart.getTotal(), 0.001); // 3 * 19.99
    }
}
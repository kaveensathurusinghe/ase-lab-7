package cart;

import product.Product;
import product.Catalog;
import inventory.InventoryService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class InventoryCartTest {
    private Catalog catalog;
    private InventoryService inventoryService;
    private InventoryCart cart;
    private Product testProduct;

    @BeforeEach
    void setUp() {
        catalog = new Catalog();
        inventoryService = mock(InventoryService.class);
        cart = new InventoryCart(catalog, inventoryService);
        testProduct = new Product("SKU123", "Test Product", 19.99);
        catalog.addProduct(testProduct);
    }

    @Test
    void testAddItemWithSufficientInventory() {
        // Arrange
        when(inventoryService.getAvailableQuantity("SKU123")).thenReturn(10);
        
        // Act
        cart.addItem("SKU123", 2);
        
        // Assert
        assertEquals(39.98, cart.getTotal(), 0.001);
        verify(inventoryService).getAvailableQuantity("SKU123");
    }

    @Test
    void testAddItemFailsWithInsufficientInventory() {
        // Arrange
        when(inventoryService.getAvailableQuantity("SKU123")).thenReturn(1);
        
        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            cart.addItem("SKU123", 2);
        });
        
        assertTrue(exception.getMessage().contains("Insufficient inventory"));
        verify(inventoryService).getAvailableQuantity("SKU123");
    }

    @Test
    void testAddItemFailsWhenNoInventory() {
        // Arrange
        when(inventoryService.getAvailableQuantity("SKU123")).thenReturn(0);
        
        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> {
            cart.addItem("SKU123", 1);
        });
    }

    @Test
    void testAddMultipleItemsChecksInventoryForEach() {
        // Arrange
        Product product2 = new Product("SKU456", "Another Product", 25.50);
        catalog.addProduct(product2);
        when(inventoryService.getAvailableQuantity("SKU123")).thenReturn(5);
        when(inventoryService.getAvailableQuantity("SKU456")).thenReturn(3);
        
        // Act
        cart.addItem("SKU123", 2);
        cart.addItem("SKU456", 1);
        
        // Assert
        assertEquals(65.48, cart.getTotal(), 0.001); // (2 * 19.99) + (1 * 25.50)
        verify(inventoryService).getAvailableQuantity("SKU123");
        verify(inventoryService).getAvailableQuantity("SKU456");
    }

    @Test
    void testAddItemToExistingCartItemChecksCurrentQuantity() {
        // Arrange
        when(inventoryService.getAvailableQuantity("SKU123")).thenReturn(5);
        cart.addItem("SKU123", 2); // Add first batch
        
        // Reset the mock to test the second call
        when(inventoryService.getAvailableQuantity("SKU123")).thenReturn(5);
        
        // Act
        cart.addItem("SKU123", 2); // Try to add 2 more (total would be 4)
        
        // Assert - should succeed as 4 <= 5
        assertEquals(79.96, cart.getTotal(), 0.001); // 4 * 19.99
    }

    @Test
    void testAddItemToExistingCartItemFailsWhenExceedsInventory() {
        // Arrange
        when(inventoryService.getAvailableQuantity("SKU123")).thenReturn(3);
        cart.addItem("SKU123", 2); // Add first batch
        
        // Reset the mock but with same available quantity
        when(inventoryService.getAvailableQuantity("SKU123")).thenReturn(3);
        
        // Act & Assert - trying to add 2 more would make total 4, but only 3 available
        assertThrows(IllegalArgumentException.class, () -> {
            cart.addItem("SKU123", 2);
        });
    }
}
package cart;

import product.Product;
import product.Catalog;
import inventory.InventoryService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CartTest {
    private Catalog catalog;
    private InventoryService inventoryService;
    private Cart cart;
    private Product testProduct;

    @BeforeEach
    void setUp() {
        catalog = new Catalog();
        inventoryService = mock(InventoryService.class);
        cart = new Cart(catalog, inventoryService);
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
        assertThrows(IllegalArgumentException.class, () -> {
            cart.addItem("SKU123", 2);
        });
    }
}

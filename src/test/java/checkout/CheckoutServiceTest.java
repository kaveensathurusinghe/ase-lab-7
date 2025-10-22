package checkout;

import cart.InventoryCart;
import cart.SimpleLineItem;
import product.Product;
import product.Catalog;
import payment.PaymentGateway;
import inventory.InventoryService;
import discount.DiscountEngine;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CheckoutServiceTest {
    private CheckoutService checkoutService;
    private PaymentGateway paymentGateway;
    private InventoryService inventoryService;
    private DiscountEngine discountEngine;
    private OrderRepository orderRepository;
    private Catalog catalog;
    private InventoryCart cart;

    @BeforeEach
    void setUp() {
        paymentGateway = mock(PaymentGateway.class);
        inventoryService = mock(InventoryService.class);
        discountEngine = mock(DiscountEngine.class);
        orderRepository = mock(OrderRepository.class);
        
        checkoutService = new CheckoutService(paymentGateway, inventoryService, discountEngine, orderRepository);
        
        catalog = new Catalog();
        cart = new InventoryCart(catalog, inventoryService);
        
        Product product = new Product("SKU123", "Test Product", 100.0);
        catalog.addProduct(product);
    }

    @Test
    void testSuccessfulCheckout() {
        // Arrange
        when(inventoryService.getAvailableQuantity("SKU123")).thenReturn(10);
        when(discountEngine.calculateTotalDiscount(any())).thenReturn(0.0);
        when(paymentGateway.charge(200.0, "token123")).thenReturn(true);
        
        cart.addItem("SKU123", 2); // 2 * 100 = 200
        
        // Act
        CheckoutResult result = checkoutService.processCheckout(cart, "token123");
        
        // Assert
        assertTrue(result.isSuccess());
        assertEquals("Order processed successfully", result.getMessage());
        assertNotNull(result.getOrder());
        verify(orderRepository).save(any(Order.class));
    }

    @Test
    void testCheckoutFailsWithPaymentFailure() {
        // Arrange
        when(inventoryService.getAvailableQuantity("SKU123")).thenReturn(10);
        when(discountEngine.calculateTotalDiscount(any())).thenReturn(0.0);
        when(paymentGateway.charge(100.0, "token123")).thenReturn(false);
        
        cart.addItem("SKU123", 1);
        
        // Act
        CheckoutResult result = checkoutService.processCheckout(cart, "token123");
        
        // Assert
        assertFalse(result.isSuccess());
        assertEquals("Payment failed", result.getMessage());
        assertNull(result.getOrder());
        verify(orderRepository, never()).save(any(Order.class));
    }

    @Test 
    void testCheckoutFailsWithInsufficientInventory() {
        // Arrange
        when(inventoryService.getAvailableQuantity("SKU123")).thenReturn(1);
        cart.addItem("SKU123", 5); // This should work initially due to mock
        when(inventoryService.getAvailableQuantity("SKU123")).thenReturn(1); // But fail at checkout validation
        
        // Act
        CheckoutResult result = checkoutService.processCheckout(cart, "token123");
        
        // Assert
        assertFalse(result.isSuccess());
        assertTrue(result.getMessage().contains("Inventory no longer available"));
        assertNull(result.getOrder());
        verify(paymentGateway, never()).charge(anyDouble(), anyString());
        verify(orderRepository, never()).save(any(Order.class));
    }
}
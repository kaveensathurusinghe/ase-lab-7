package checkout;

import cart.InventoryCart;
import payment.PaymentGateway;
import inventory.InventoryService;
import discount.DiscountEngine;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class CheckoutService {
    private final PaymentGateway paymentGateway;
    private final InventoryService inventoryService;
    private final DiscountEngine discountEngine;
    private final OrderRepository orderRepository;

    public CheckoutService(PaymentGateway paymentGateway, InventoryService inventoryService, 
                          DiscountEngine discountEngine, OrderRepository orderRepository) {
        this.paymentGateway = paymentGateway;
        this.inventoryService = inventoryService;
        this.discountEngine = discountEngine;
        this.orderRepository = orderRepository;
    }

    public CheckoutResult processCheckout(InventoryCart cart, String paymentToken) {
        // Validate inventory is still available
        for (var entry : cart.getItems().entrySet()) {
            String sku = entry.getKey();
            int requested = entry.getValue().getQuantity();
            int available = inventoryService.getAvailableQuantity(sku);
            
            if (requested > available) {
                return new CheckoutResult(false, "Inventory no longer available for " + sku, null);
            }
        }
        
        // Calculate total with discounts
        double subtotal = cart.getTotal();
        double totalDiscount = discountEngine.calculateTotalDiscount(new ArrayList<>(cart.getItems().values()));
        double finalAmount = subtotal - totalDiscount;
        
        // Process payment
        boolean paymentSuccess = paymentGateway.charge(finalAmount, paymentToken);
        if (!paymentSuccess) {
            return new CheckoutResult(false, "Payment failed", null);
        }
        
        // Create order record
        Order order = new Order(
            generateOrderId(),
            new ArrayList<>(cart.getItems().values()),
            subtotal,
            totalDiscount,
            finalAmount,
            LocalDateTime.now()
        );
        
        orderRepository.save(order);
        
        return new CheckoutResult(true, "Order processed successfully", order);
    }
    
    private String generateOrderId() {
        return "ORDER-" + System.currentTimeMillis();
    }
}

package checkout;

import cart.SimpleLineItem;
import java.time.LocalDateTime;
import java.util.List;

public class Order {
    private final String orderId;
    private final List<SimpleLineItem> lineItems;
    private final double subtotal;
    private final double totalDiscount;
    private final double finalAmount;
    private final LocalDateTime timestamp;

    public Order(String orderId, List<SimpleLineItem> lineItems, double subtotal, 
                 double totalDiscount, double finalAmount, LocalDateTime timestamp) {
        this.orderId = orderId;
        this.lineItems = lineItems;
        this.subtotal = subtotal;
        this.totalDiscount = totalDiscount;
        this.finalAmount = finalAmount;
        this.timestamp = timestamp;
    }

    // Getters
    public String getOrderId() { return orderId; }
    public List<SimpleLineItem> getLineItems() { return lineItems; }
    public double getSubtotal() { return subtotal; }
    public double getTotalDiscount() { return totalDiscount; }
    public double getFinalAmount() { return finalAmount; }
    public LocalDateTime getTimestamp() { return timestamp; }
}

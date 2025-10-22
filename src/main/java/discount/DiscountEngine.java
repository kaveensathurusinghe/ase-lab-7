package discount;

import cart.SimpleLineItem;
import java.util.List;

public class DiscountEngine {
    
    public double calculateBulkDiscount(SimpleLineItem lineItem) {
        if (lineItem.getQuantity() >= 10) {
            return lineItem.getSubtotal() * 0.10; // 10% discount
        }
        return 0.0;
    }
    
    public double calculateOrderDiscount(List<SimpleLineItem> lineItems) {
        double subtotal = lineItems.stream()
            .mapToDouble(SimpleLineItem::getSubtotal)
            .sum();
            
        if (subtotal >= 1000.0) {
            return subtotal * 0.05; // 5% discount
        }
        return 0.0;
    }
    
    public double calculateTotalDiscount(List<SimpleLineItem> lineItems) {
        // First calculate bulk discounts for each line item
        double totalBulkDiscount = lineItems.stream()
            .mapToDouble(this::calculateBulkDiscount)
            .sum();
            
        // Calculate subtotal after bulk discounts
        double subtotalAfterBulkDiscount = lineItems.stream()
            .mapToDouble(item -> item.getSubtotal() - calculateBulkDiscount(item))
            .sum();
            
        // Calculate order discount based on discounted subtotal
        double orderDiscount = 0.0;
        if (subtotalAfterBulkDiscount >= 1000.0) {
            orderDiscount = subtotalAfterBulkDiscount * 0.05;
        }
        
        return totalBulkDiscount + orderDiscount;
    }
}

package discount;

import cart.SimpleLineItem;
import product.Product;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;
import java.util.Arrays;
import java.util.Collections;

class DiscountEngineTest {
    private DiscountEngine discountEngine;
    private Product expensiveProduct;
    private Product cheapProduct;

    @BeforeEach
    void setUp() {
        discountEngine = new DiscountEngine();
        expensiveProduct = new Product("EXPENSIVE", "Expensive Item", 100.0);
        cheapProduct = new Product("CHEAP", "Cheap Item", 50.0);
    }

    @Test
    void testNoBulkDiscountWhenQuantityLessThan10() {
        SimpleLineItem lineItem = new SimpleLineItem(expensiveProduct, 5); // 5 * 100 = 500
        
        double discount = discountEngine.calculateBulkDiscount(lineItem);
        
        assertEquals(0.0, discount, 0.001);
    }

    @Test
    void testBulkDiscountWhenQuantityIs10OrMore() {
        SimpleLineItem lineItem = new SimpleLineItem(expensiveProduct, 10); // 10 * 100 = 1000, 10% off = 100
        
        double discount = discountEngine.calculateBulkDiscount(lineItem);
        
        assertEquals(100.0, discount, 0.001); // 10% of 1000
    }

    @Test
    void testBulkDiscountWithQuantity15() {
        SimpleLineItem lineItem = new SimpleLineItem(cheapProduct, 15); // 15 * 50 = 750, 10% off = 75
        
        double discount = discountEngine.calculateBulkDiscount(lineItem);
        
        assertEquals(75.0, discount, 0.001); // 10% of 750
    }

    @Test
    void testNoOrderDiscountWhenTotalLessThan1000() {
        SimpleLineItem item1 = new SimpleLineItem(expensiveProduct, 5); // 500
        SimpleLineItem item2 = new SimpleLineItem(cheapProduct, 8); // 400
        // Total = 900
        
        double discount = discountEngine.calculateOrderDiscount(Arrays.asList(item1, item2));
        
        assertEquals(0.0, discount, 0.001);
    }

    @Test
    void testOrderDiscountWhenTotalIs1000OrMore() {
        SimpleLineItem item1 = new SimpleLineItem(expensiveProduct, 8); // 800
        SimpleLineItem item2 = new SimpleLineItem(cheapProduct, 5); // 250
        // Total = 1050, 5% off = 52.5
        
        double discount = discountEngine.calculateOrderDiscount(Arrays.asList(item1, item2));
        
        assertEquals(52.5, discount, 0.001); // 5% of 1050
    }

    @Test
    void testOrderDiscountWithExactly1000() {
        SimpleLineItem item = new SimpleLineItem(expensiveProduct, 10); // 1000
        
        double discount = discountEngine.calculateOrderDiscount(Collections.singletonList(item));
        
        assertEquals(50.0, discount, 0.001); // 5% of 1000
    }

    @Test
    void testTotalDiscountCalculationWithBothDiscounts() {
        // Line item eligible for bulk discount: 12 * 100 = 1200, bulk discount = 120
        SimpleLineItem bulkItem = new SimpleLineItem(expensiveProduct, 12);
        SimpleLineItem regularItem = new SimpleLineItem(cheapProduct, 2); // 100
        
        // After bulk discount: (1200 - 120) + 100 = 1180
        // Order discount: 5% of 1180 = 59
        // Total discount: 120 + 59 = 179
        
        double totalDiscount = discountEngine.calculateTotalDiscount(Arrays.asList(bulkItem, regularItem));
        
        assertEquals(179.0, totalDiscount, 0.001);
    }

    @Test
    void testEmptyCartHasNoDiscount() {
        double discount = discountEngine.calculateOrderDiscount(Collections.emptyList());
        assertEquals(0.0, discount, 0.001);
    }
}
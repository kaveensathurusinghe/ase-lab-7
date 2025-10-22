package cart;

import product.Product;
import product.Catalog;
import inventory.InventoryService;
import java.util.HashMap;
import java.util.Map;

public class InventoryCart {
    private final Catalog catalog;
    private final InventoryService inventoryService;
    private final Map<String, SimpleLineItem> items = new HashMap<>();

    public InventoryCart(Catalog catalog, InventoryService inventoryService) {
        this.catalog = catalog;
        this.inventoryService = inventoryService;
    }

    public void addItem(String sku, int quantity) {
        if (quantity <= 0) {
            throw new IllegalArgumentException("Quantity must be positive");
        }
        
        Product product = catalog.findProduct(sku);
        if (product == null) {
            throw new IllegalArgumentException("Product not found: " + sku);
        }

        // Check inventory availability
        int available = inventoryService.getAvailableQuantity(sku);
        int currentQuantity = items.containsKey(sku) ? items.get(sku).getQuantity() : 0;
        int totalRequested = currentQuantity + quantity;
        
        if (totalRequested > available) {
            throw new IllegalArgumentException(
                "Insufficient inventory for " + sku +
                ". Requested: " + quantity + 
                ", Current in cart: " + currentQuantity +
                ", Available: " + available
            );
        }
        
        SimpleLineItem existingItem = items.get(sku);
        if (existingItem != null) {
            items.put(sku, new SimpleLineItem(product, existingItem.getQuantity() + quantity));
        } else {
            items.put(sku, new SimpleLineItem(product, quantity));
        }
    }

    public void removeItem(String sku) {
        items.remove(sku);
    }

    public double getTotal() {
        return items.values().stream()
            .mapToDouble(SimpleLineItem::getSubtotal)
            .sum();
    }

    public int getItemCount() {
        return items.size();
    }

    public Map<String, SimpleLineItem> getItems() {
        return new HashMap<>(items);
    }
}
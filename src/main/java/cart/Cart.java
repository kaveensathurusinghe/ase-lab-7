package cart;

import product.Product;
import product.Catalog;
import inventory.InventoryService;
import java.util.HashMap;
import java.util.Map;

public class Cart {
    private final Catalog catalog;
    private final InventoryService inventoryService;
    private final Map<String, LineItem> items = new HashMap<>();

    public Cart(Catalog catalog, InventoryService inventoryService) {
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

        // Check inventory
        int available = inventoryService.getAvailableQuantity(sku);
        int currentQuantity = items.containsKey(sku) ? items.get(sku).getQuantity() : 0;
        
        if (currentQuantity + quantity > available) {
            throw new IllegalArgumentException(
                "Insufficient inventory for " + sku +
                ". Requested: " + quantity + ", Available: " + available
            );
        }
        
        LineItem existingItem = items.get(sku);
        if (existingItem != null) {
            items.put(sku, existingItem.withQuantity(existingItem.getQuantity() + quantity));
        } else {
            items.put(sku, new LineItem(product, quantity));
        }
    }

    public void removeItem(String sku) {
        items.remove(sku);
    }

    public void updateQuantity(String sku, int newQuantity) {
        if (newQuantity <= 0) {
            removeItem(sku);
            return;
        }
        
        LineItem existing = items.get(sku);
        if (existing != null) {
            items.put(sku, existing.withQuantity(newQuantity));
        }
    }

    public double getTotal() {
        return items.values().stream()
            .mapToDouble(LineItem::getSubtotal)
            .sum();
    }

    public int getItemCount() {
        return items.size();
    }

    public Map<String, LineItem> getItems() {
        return new HashMap<>(items);
    }
}
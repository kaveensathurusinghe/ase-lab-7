package cart;

import product.Product;
import product.Catalog;
import java.util.HashMap;
import java.util.Map;

public class SimpleCart {
    private final Catalog catalog;
    private final Map<String, SimpleLineItem> items = new HashMap<>();

    public SimpleCart(Catalog catalog) {
        this.catalog = catalog;
    }

    public void addItem(String sku, int quantity) {
        if (quantity <= 0) {
            throw new IllegalArgumentException("Quantity must be positive");
        }
        
        Product product = catalog.findProduct(sku);
        if (product == null) {
            throw new IllegalArgumentException("Product not found: " + sku);
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
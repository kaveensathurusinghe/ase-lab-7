package inventory;

public interface InventoryService {
    int getAvailableQuantity(String sku);
    boolean reserve(String sku, int quantity);
}

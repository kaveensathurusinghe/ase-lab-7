package inventory;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class InventoryServiceImpl implements InventoryService {
    private final Map<String, Integer> inventory = new HashMap<>();
    private final Map<String, Lock> locks = new HashMap<>();

    public void addStock(String sku, int quantity) {
        if (quantity <= 0) {
            throw new IllegalArgumentException("Quantity must be positive");
        }
        inventory.put(sku, inventory.getOrDefault(sku, 0) + quantity);
        locks.putIfAbsent(sku, new ReentrantLock());
    }

    @Override
    public int getAvailableQuantity(String sku) {
        return inventory.getOrDefault(sku, 0);
    }

    @Override
    public boolean reserve(String sku, int quantity) {
        Lock lock = locks.computeIfAbsent(sku, k -> new ReentrantLock());
        lock.lock();
        try {
            int available = getAvailableQuantity(sku);
            if (available >= quantity) {
                inventory.put(sku, available - quantity);
                return true;
            }
            return false;
        } finally {
            lock.unlock();
        }
    }

    public void release(String sku, int quantity) {
        Lock lock = locks.get(sku);
        if (lock != null) {
            lock.lock();
            try {
                inventory.put(sku, inventory.getOrDefault(sku, 0) + quantity);
            } finally {
                lock.unlock();
            }
        }
    }
}
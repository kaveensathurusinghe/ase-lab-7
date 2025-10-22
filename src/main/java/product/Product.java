package product;

public class Product {
    private final String sku;
    private final String name;
    private final double price;

    public Product(String sku, String name, double price) {
        if (sku == null || sku.trim().isEmpty()) {
            throw new IllegalArgumentException("SKU cannot be null or empty");
        }
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Name cannot be null or empty");
        }
        if (price < 0) {
            throw new IllegalArgumentException("Price cannot be negative");
        }
        this.sku = sku.trim();
        this.name = name.trim();
        this.price = price;
    }

    // Getters only - immutable object
    public String getSku() { return sku; }
    public String getName() { return name; }
    public double getPrice() { return price; }
}
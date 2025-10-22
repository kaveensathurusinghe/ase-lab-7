package cart;

import product.Product;

public class LineItem {
    private final Product product;
    private final int quantity;

    public LineItem(Product product, int quantity) {
        if (product == null) {
            throw new IllegalArgumentException("Product cannot be null");
        }
        if (quantity <= 0) {
            throw new IllegalArgumentException("Quantity must be positive");
        }
        this.product = product;
        this.quantity = quantity;
    }

    public Product getProduct() { return product; }
    public int getQuantity() { return quantity; }
    public double getSubtotal() { return product.getPrice() * quantity; }

    public LineItem withQuantity(int newQuantity) {
        return new LineItem(product, newQuantity);
    }
}
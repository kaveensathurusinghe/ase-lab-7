package product;

import java.util.HashMap;
import java.util.Map;

public class Catalog {
    private Map<String, Product> products = new HashMap<>();

    public void addProduct(Product product) {
        products.put(product.getSku(), product);
    }

    public Product findProduct(String sku) {
        return products.get(sku);
    }

    public boolean containsProduct(String sku) {
        return products.containsKey(sku);
    }

    public int getProductCount() {
        return products.size();
    }
}
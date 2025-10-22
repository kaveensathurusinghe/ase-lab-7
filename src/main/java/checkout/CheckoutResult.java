package checkout;

public class CheckoutResult {
    private final boolean success;
    private final String message;
    private final Order order;

    public CheckoutResult(boolean success, String message, Order order) {
        this.success = success;
        this.message = message;
        this.order = order;
    }

    public boolean isSuccess() { return success; }
    public String getMessage() { return message; }
    public Order getOrder() { return order; }
}
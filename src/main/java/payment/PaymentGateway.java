package payment;

public interface PaymentGateway {
    boolean charge(double amount, String paymentToken);
    String createPaymentIntent(double amount);
}
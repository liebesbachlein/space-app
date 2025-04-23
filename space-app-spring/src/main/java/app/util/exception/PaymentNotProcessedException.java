package app.util.exception;

public class PaymentNotProcessedException extends RuntimeException {
    public PaymentNotProcessedException(String message) {
        super(message);
    }
}

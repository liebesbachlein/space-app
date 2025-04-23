package app.util.validation.exception;

public class NonUniquePropertyException extends RuntimeException {
    public NonUniquePropertyException(String message) {
        super(message);
    }
}

package app.util.exception;

public class NonUniqueNameException extends RuntimeException {
    public NonUniqueNameException(String msg) {
        super(msg);
    }
}

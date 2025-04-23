package app.util.exception.auth;

public class InvalidOrExpiredRefreshTokenException extends RuntimeException {
    public InvalidOrExpiredRefreshTokenException(String message) {
        super(message);
    }
}

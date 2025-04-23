package app.util.exception.auth;

public class UnauthorizedOperationException extends RuntimeException{
    public UnauthorizedOperationException(String msg) {
        super(msg);
    }
}

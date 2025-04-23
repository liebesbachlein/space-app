package app.util.exception;

public class ReservationConflictException extends RuntimeException {
    public ReservationConflictException(String msg) {
        super(msg);
    }
}

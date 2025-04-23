package app.util.exception;

public class SessionInUseOrClosedException extends RuntimeException {

  public SessionInUseOrClosedException(String message) {
    super(message);
  }
}

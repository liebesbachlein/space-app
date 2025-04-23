package app.util.exception;

public class SessionNotExecutedException extends RuntimeException {

  public SessionNotExecutedException(String message) {
    super(message);
  }
}

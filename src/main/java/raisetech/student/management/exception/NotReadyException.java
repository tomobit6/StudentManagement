package raisetech.student.management.exception;

public class NotReadyException extends Exception {

  public NotReadyException() {
    super();
  }

  public NotReadyException(String message) {
    super(message);
  }

  public NotReadyException(String message, Throwable cause) {
    super(message, cause);
  }

  public NotReadyException(Throwable cause) {
    super(cause);
  }
}

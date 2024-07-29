package ml.volder.unikapi;

public class NotSupportedException extends RuntimeException{

    public NotSupportedException() {
        super();
    }
    public NotSupportedException(String message) {
        super(message);
    }

    public NotSupportedException(String message, Throwable cause) {
        super(message, cause);
    }

    public NotSupportedException(Throwable cause) {
        super(cause);
    }
}

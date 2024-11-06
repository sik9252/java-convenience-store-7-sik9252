package store.exception;

public class StoreException extends IllegalArgumentException {
    private static final String ERROR_PREFIX = "[ERROR] ";

    protected StoreException(String message) {
        super(ERROR_PREFIX + message);
    }
}

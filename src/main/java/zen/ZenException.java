package zen;

/** Represents a user-facing error that Zen can display and recover from. */
public class ZenException extends Exception {
    /**
     * Creates an exception containing a user-facing error message.
     *
     * @param message explanation of the error
     */
    public ZenException(String message) {
        super(message);
    }
}

package net.netau.vasyoid;

/**
 * Exception thrown by a LightFuture object when a task could not be completed.
 */
public class LightExecutionException extends Exception {

    /**
     * Constructor that gets a message.
     * @param message message about an error.
     */
    LightExecutionException(String message) {
        super(message);
    }
}

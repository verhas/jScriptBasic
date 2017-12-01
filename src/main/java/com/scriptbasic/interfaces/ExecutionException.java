package com.scriptbasic.interfaces;

/**
 * @author Peter Verhas
 * date June 22, 2012
 */
@SuppressWarnings("serial")
public abstract class ExecutionException extends Exception {

    public ExecutionException() {
        super();
    }

    public ExecutionException(final String message, final Throwable cause) {
        super(message, cause);
    }

    public ExecutionException(final String message) {
        super(message);
    }

    public ExecutionException(final Throwable cause) {
        super(cause);
    }

}

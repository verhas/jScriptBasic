package com.scriptbasic.api;

public class ScriptBasicException extends Exception {

    private static final long serialVersionUID = 1L;

    public ScriptBasicException() {
        super();
    }

    public ScriptBasicException(final String message, final Throwable cause) {
        super(message, cause);
    }

    public ScriptBasicException(final String message) {
        super(message);
    }

    public ScriptBasicException(final Throwable cause) {
        super(cause);
    }

}

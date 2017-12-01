package com.scriptbasic.api;

public class ScriptBasicException extends Exception {

    private static final long serialVersionUID = 1L;

    public ScriptBasicException() {
        super();
    }

    public ScriptBasicException(String message, Throwable cause) {
        super(message, cause);
    }

    public ScriptBasicException(String message) {
        super(message);
    }

    public ScriptBasicException(Throwable cause) {
        super(cause);
    }

}

package com.scriptbasic.errors;

public class BasicInterpreterInternalError extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public BasicInterpreterInternalError(final String arg0) {
        super(arg0);
    }

    public BasicInterpreterInternalError(final String arg0, Throwable e) {
        super(arg0, e);
    }
}

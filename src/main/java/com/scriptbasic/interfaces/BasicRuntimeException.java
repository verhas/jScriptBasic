package com.scriptbasic.interfaces;

public class BasicRuntimeException extends ExecutionException {

    private static final long serialVersionUID = -2861269478069129351L;

    public BasicRuntimeException() {
    }

    public BasicRuntimeException(final String arg0) {
        super(arg0);
    }

    public BasicRuntimeException(final Throwable arg0) {
        super(arg0);
    }

    public BasicRuntimeException(final String arg0, final Throwable arg1) {
        super(arg0, arg1);
    }

}

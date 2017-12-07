package com.scriptbasic.interfaces;

import com.scriptbasic.api.ScriptBasicException;

/**
 * This is the exception that the BASIC program throws when there is some error during the execution. This is also
 * the exception that extension functions should throw if they can not perform the proper action.
 */
public class BasicRuntimeException extends ScriptBasicException {

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

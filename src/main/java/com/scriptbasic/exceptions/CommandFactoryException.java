package com.scriptbasic.exceptions;

import com.scriptbasic.interfaces.AnalysisException;

public class CommandFactoryException extends AnalysisException {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    public CommandFactoryException() {
        super();
    }

    public CommandFactoryException(final String arg0, final Throwable arg1) {
        super(arg0, arg1);
    }

    public CommandFactoryException(final String arg0) {
        super(arg0);
    }

    public CommandFactoryException(final Throwable arg0) {
        super(arg0);
    }

}

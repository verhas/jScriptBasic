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

    public CommandFactoryException(String arg0, Throwable arg1) {
        super(arg0, arg1);
    }

    public CommandFactoryException(String arg0) {
        super(arg0);
    }

    public CommandFactoryException(Throwable arg0) {
        super(arg0);
    }

}

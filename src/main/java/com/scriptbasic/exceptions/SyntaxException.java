package com.scriptbasic.exceptions;

/**
 * Exception to throw when there is some error during the syntax analysis
 * related to the analysis itself and not the reading of the source.
 * 
 * @author Peter Verhas
 * 
 */
public abstract class SyntaxException extends GeneralAnalysisException {

    public SyntaxException() {
        super();
    }

    public SyntaxException(final String arg0, final Throwable arg1) {
        super(arg0, arg1);
    }

    public SyntaxException(final String arg0) {
        super(arg0);
    }

    public SyntaxException(final Throwable arg0) {
        super(arg0);
    }

}

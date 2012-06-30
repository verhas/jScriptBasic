package com.scriptbasic.exceptions;

/**
 * Exception to throw when there is some error during the lexical analysis
 * related to the analysis itself and not the reading of the source.
 * 
 * @author Peter Verhas
 * 
 */
public abstract class LexicalException extends GeneralAnalysisException {

    public LexicalException() {
        super();
    }

    public LexicalException(final String arg0, final Throwable arg1) {
        super(arg0, arg1);
    }

    public LexicalException(final String arg0) {
        super(arg0);
    }

    public LexicalException(final Throwable arg0) {
        super(arg0);
    }

    @Override
    public abstract String getFileName();

    @Override
    public abstract int getLineNumber();

    @Override
    public abstract int getPosition();
}

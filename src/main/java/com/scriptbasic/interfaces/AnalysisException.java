package com.scriptbasic.interfaces;

/**
 * @author Peter Verhas
 * date Jun 18, 2012
 */
@SuppressWarnings("serial")
public abstract class AnalysisException extends Exception {

    public AnalysisException() {
        super();
    }

    public AnalysisException(final String message, final Throwable cause) {
        super(message, cause);
    }

    public AnalysisException(final String message) {
        super(message);
    }

    public AnalysisException(final Throwable cause) {
        super(cause);
    }

}

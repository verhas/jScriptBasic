/**
 * 
 */
package com.scriptbasic.interfaces;

/**
 * @author Peter Verhas
 * @date Jun 18, 2012
 * 
 */
@SuppressWarnings("serial")
public abstract class AnalysisException extends Exception {

    public AnalysisException() {
        super();
    }

    public AnalysisException(String message, Throwable cause) {
        super(message, cause);
    }

    public AnalysisException(String message) {
        super(message);
    }

    public AnalysisException(Throwable cause) {
        super(cause);
    }

}

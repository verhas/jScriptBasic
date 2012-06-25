/**
 * 
 */
package com.scriptbasic.interfaces;

/**
 * @author Peter Verhas
 * @date June 22, 2012
 * 
 */
public abstract class ExecutionException extends Exception {

    public ExecutionException() {
        super();
    }

    public ExecutionException(String message, Throwable cause) {
        super(message, cause);
    }

    public ExecutionException(String message) {
        super(message);
    }

    public ExecutionException(Throwable cause) {
        super(cause);
    }

}

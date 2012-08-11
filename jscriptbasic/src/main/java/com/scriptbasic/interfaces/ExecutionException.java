/**
 *
 */
package com.scriptbasic.interfaces;

/**
 * @author Peter Verhas date June 22, 2012
 * 
 */
public abstract class ExecutionException extends Exception implements
        SourceLocationBound {
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

    private String fileName;
    private int lineNumber;
    private int position;



    /**
     * @param fileName the fileName to set
     */
    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    /**
     * @param lineNumber the lineNumber to set
     */
    public void setLineNumber(int lineNumber) {
        this.lineNumber = lineNumber;
    }

    /**
     * @param position the position to set
     */
    public void setPosition(int position) {
        this.position = position;
    }

    @Override
    public String getFileName() {
        return this.fileName;
    }

    @Override
    public int getLineNumber() {
        return this.lineNumber;
    }

    @Override
    public int getPosition() {
        return this.position;
    }
}

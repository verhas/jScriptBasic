package com.scriptbasic.interfaces;

/**
 * Exception to throw when there is some error during the syntax analysis
 * related to the analysis itself and not the reading of the source.
 * 
 * @author Peter Verhas
 * 
 */
public abstract class SyntaxException extends Exception {

    private static final long serialVersionUID = -5235689307096026119L;

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

    private String fileName;
    private int lineNumber;
    private int position;

    public void setLocation(final LexicalElement le) {
        this.fileName = le.fileName();
        this.lineNumber = le.lineNumber();
        this.position = le.position();
    }

    public String fileName() {
        return this.fileName;
    }

    public int lineNumber() {
        return this.lineNumber;
    }

    public int position() {
        return this.position;
    }
}

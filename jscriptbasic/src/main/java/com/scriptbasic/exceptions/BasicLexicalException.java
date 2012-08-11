package com.scriptbasic.exceptions;
import com.scriptbasic.interfaces.Reader;
public class BasicLexicalException extends LexicalException {
    private static final long serialVersionUID = 8068731911556013119L;
    public BasicLexicalException() {
        super();
    }
    public BasicLexicalException(final String arg0, final Throwable arg1) {
        super(arg0, arg1);
    }
    public BasicLexicalException(final String arg0) {
        super(arg0);
    }
    public BasicLexicalException(final Throwable arg0) {
        super(arg0);
    }
    private String fileName;
    private int lineNumber;
    private int position;
    public void setPosition(final Reader reader) {
        this.fileName = reader.getFileName();
        this.lineNumber = reader.getLineNumber();
        this.position = reader.getPosition();
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
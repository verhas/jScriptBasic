package com.scriptbasic.exceptions;
import com.scriptbasic.interfaces.LexicalElement;
public class GenericSyntaxException extends SyntaxException {
    private static final long serialVersionUID = 1L;
    public GenericSyntaxException() {
        super();
    }
    public GenericSyntaxException(final String arg0, final Throwable arg1) {
        super(arg0, arg1);
    }
    public GenericSyntaxException(final Throwable arg0) {
        super(arg0);
    }
    public GenericSyntaxException(final String s) {
        super(s);
    }
    public GenericSyntaxException(final String s, LexicalElement le) {
        super(s);
        if (le != null) {
            setLocation(le);
        }
    }
    public GenericSyntaxException(final String s, LexicalElement le,
            final Throwable e) {
        super(s, e);
        if (le != null) {
            setLocation(le);
        }
    }
    public String toString() {
        return getMessage() + " " + getFileName() + "(" + getLineNumber()
                + "):" + getPosition();
    }
}
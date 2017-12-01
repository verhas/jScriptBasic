package com.scriptbasic.interfaces;

import com.scriptbasic.exceptions.SyntaxException;
import com.scriptbasic.interfaces.LexicalElement;

public class BasicSyntaxException extends SyntaxException {

    private static final long serialVersionUID = 1L;

    public BasicSyntaxException() {
        super();
    }

    public BasicSyntaxException(final String arg0, final Throwable arg1) {
        super(arg0, arg1);
    }

    public BasicSyntaxException(final Throwable arg0) {
        super(arg0);
    }

    public BasicSyntaxException(final String s) {
        super(s);
    }

    public BasicSyntaxException(final String s, LexicalElement le) {
        super(s);
        if (le != null) {
            setLocation(le);
        }
    }

    public BasicSyntaxException(final String s, LexicalElement le,
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

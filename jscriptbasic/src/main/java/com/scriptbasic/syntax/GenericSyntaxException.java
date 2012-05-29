package com.scriptbasic.syntax;

import com.scriptbasic.interfaces.SyntaxException;

public class GenericSyntaxException extends SyntaxException {

    private static final long serialVersionUID = 1L;

    public GenericSyntaxException() {
        super();
    }

    public GenericSyntaxException(String arg0, Throwable arg1) {
        super(arg0, arg1);
    }

    public GenericSyntaxException(Throwable arg0) {
        super(arg0);
    }

    public GenericSyntaxException(String s) {
        super(s);
    }

}

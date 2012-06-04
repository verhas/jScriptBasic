package com.scriptbasic.syntax;

import com.scriptbasic.interfaces.SyntaxException;

public class KeywordNotImplemented extends SyntaxException {

    private static final long serialVersionUID = 1L;

    public KeywordNotImplemented(final String commandKeyword) {
        super(commandKeyword);
    }

}

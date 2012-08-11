package com.scriptbasic.exceptions;
public class KeywordNotImplementedException extends SyntaxException {
    private static final long serialVersionUID = 1L;
    public KeywordNotImplementedException(final String commandKeyword) {
        super(commandKeyword);
    }
}
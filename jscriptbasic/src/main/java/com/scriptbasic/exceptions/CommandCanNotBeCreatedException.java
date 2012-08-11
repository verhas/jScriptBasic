package com.scriptbasic.exceptions;
public class CommandCanNotBeCreatedException extends SyntaxException {
    private static final long serialVersionUID = -7523742170361842550L;
    public CommandCanNotBeCreatedException(final String commandKeyword,
            final Exception e) {
        super(commandKeyword, e);
    }
}
package com.scriptbasic.syntax;

import com.scriptbasic.interfaces.SyntaxException;

public class CommandCanNotBeCreatedException extends SyntaxException {

    public CommandCanNotBeCreatedException(String commandKeyword,
            Exception e) {
        super(commandKeyword, e);
    }

}

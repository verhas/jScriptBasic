package com.scriptbasic.syntax.commandanalyzers.commands;

import com.scriptbasic.exceptions.SyntaxException;
import com.scriptbasic.interfaces.Command;
import com.scriptbasic.syntax.commandanalyzers.AbstractCommandAnalyzer;

public class CommandAnalyzerCall extends AbstractCommandAnalyzer {

    @Override
    public Command analyze() throws SyntaxException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    protected String getName() {
        return "CALL";
    }

}

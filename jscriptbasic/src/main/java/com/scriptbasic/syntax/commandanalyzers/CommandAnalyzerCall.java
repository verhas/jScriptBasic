package com.scriptbasic.syntax.commandanalyzers;

import com.scriptbasic.exceptions.SyntaxException;
import com.scriptbasic.interfaces.Command;

public class CommandAnalyzerCall extends AbstractCommandAnalyzer {

    @Override
    public Command analyze() throws SyntaxException {
        // TODO implement the analysis of a line that calls a procedure
        return null;
    }

    @Override
    protected String getName() {
        return "CALL";
    }

}

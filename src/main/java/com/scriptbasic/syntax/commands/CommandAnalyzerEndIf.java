package com.scriptbasic.syntax.commands;

import com.scriptbasic.executors.commands.CommandEndIf;
import com.scriptbasic.interfaces.AnalysisException;
import com.scriptbasic.interfaces.Command;

public class CommandAnalyzerEndIf extends AbstractCommandAnalyzerIfElseKind {

    @Override
    public Command analyze() throws AnalysisException {
        CommandEndIf node = new CommandEndIf();
        consumeEndOfLine();
        registerAndPopNode(node);
        return node;
    }

}

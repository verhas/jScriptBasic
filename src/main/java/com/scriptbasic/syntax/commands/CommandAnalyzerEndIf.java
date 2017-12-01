package com.scriptbasic.syntax.commands;

import com.scriptbasic.executors.commands.CommandEndIf;
import com.scriptbasic.factories.Context;
import com.scriptbasic.interfaces.AnalysisException;
import com.scriptbasic.interfaces.Command;

public class CommandAnalyzerEndIf extends AbstractCommandAnalyzerIfElseKind {

    public CommandAnalyzerEndIf(Context ctx) {
        super(ctx);
    }

    @Override
    public Command analyze() throws AnalysisException {
        CommandEndIf node = new CommandEndIf();
        consumeEndOfLine();
        registerAndPopNode(node);
        return node;
    }

}

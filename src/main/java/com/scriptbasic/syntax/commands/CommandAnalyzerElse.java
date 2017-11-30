package com.scriptbasic.syntax.commands;

import com.scriptbasic.executors.commands.CommandElse;
import com.scriptbasic.factories.Context;
import com.scriptbasic.interfaces.AnalysisException;
import com.scriptbasic.interfaces.Command;

public class CommandAnalyzerElse extends AbstractCommandAnalyzerIfElseKind {

    public CommandAnalyzerElse(Context ctx) {
        super(ctx);
    }

    @Override
    public Command analyze() throws AnalysisException {
        CommandElse node = new CommandElse();
        consumeEndOfLine();
        registerAndSwapNode(node);
        return node;
    }

}

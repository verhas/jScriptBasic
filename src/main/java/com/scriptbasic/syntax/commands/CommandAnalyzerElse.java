package com.scriptbasic.syntax.commands;

import com.scriptbasic.context.Context;
import com.scriptbasic.executors.commands.CommandElse;
import com.scriptbasic.interfaces.AnalysisException;
import com.scriptbasic.api.Command;

public class CommandAnalyzerElse extends AbstractCommandAnalyzerIfElseKind {

    public CommandAnalyzerElse(final Context ctx) {
        super(ctx);
    }

    @Override
    public Command analyze() throws AnalysisException {
        final CommandElse node = new CommandElse();
        consumeEndOfLine();
        registerAndSwapNode(node);
        return node;
    }

}

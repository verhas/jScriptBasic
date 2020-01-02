package com.scriptbasic.syntax.commands;

import com.scriptbasic.context.Context;
import com.scriptbasic.executors.commands.CommandElse;
import com.scriptbasic.interfaces.AnalysisException;
import com.scriptbasic.spi.Command;

public class CommandAnalyzerElse extends AbstractCommandAnalyzerIfElseKind {

    public CommandAnalyzerElse(final Context ctx) {
        super(ctx);
    }

    @Override
    public Command analyze() throws AnalysisException {
        final var node = new CommandElse();
        consumeEndOfStatement();
        registerAndSwapNode(node);
        return node;
    }

}

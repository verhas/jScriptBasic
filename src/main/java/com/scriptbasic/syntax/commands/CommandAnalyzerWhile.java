package com.scriptbasic.syntax.commands;

import com.scriptbasic.context.Context;
import com.scriptbasic.executors.commands.CommandWhile;
import com.scriptbasic.interfaces.AnalysisException;
import com.scriptbasic.spi.Command;

public class CommandAnalyzerWhile extends AbstractCommandAnalyzer {

    public CommandAnalyzerWhile(final Context ctx) {
        super(ctx);
    }

    @Override
    public Command analyze() throws AnalysisException {
        final var node = new CommandWhile();
        final var condition = analyzeExpression();
        consumeEndOfStatement();
        node.setCondition(condition);
        pushNode(node);
        return node;
    }

}

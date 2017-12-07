package com.scriptbasic.syntax.commands;

import com.scriptbasic.context.Context;
import com.scriptbasic.executors.commands.CommandWhile;
import com.scriptbasic.interfaces.AnalysisException;
import com.scriptbasic.api.Command;
import com.scriptbasic.interfaces.Expression;

public class CommandAnalyzerWhile extends AbstractCommandAnalyzer {

    public CommandAnalyzerWhile(final Context ctx) {
        super(ctx);
    }

    @Override
    public Command analyze() throws AnalysisException {
        final CommandWhile node = new CommandWhile();
        final Expression condition = analyzeExpression();
        consumeEndOfLine();
        node.setCondition(condition);
        pushNode(node);
        return node;
    }

}

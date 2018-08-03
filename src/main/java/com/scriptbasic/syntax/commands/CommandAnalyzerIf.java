package com.scriptbasic.syntax.commands;

import com.scriptbasic.context.Context;
import com.scriptbasic.executors.commands.CommandIf;
import com.scriptbasic.interfaces.AnalysisException;
import com.scriptbasic.spi.Command;
import com.scriptbasic.interfaces.Expression;

/**
 * @author Peter Verhas
 * date June 16, 2012
 */
public class CommandAnalyzerIf extends AbstractCommandAnalyzerIfKind {

    public CommandAnalyzerIf(final Context ctx) {
        super(ctx);
    }

    protected Command createNode(final Expression condition) {
        final var node = new CommandIf();
        node.setCondition(condition);
        pushNode(node);
        return node;
    }
}

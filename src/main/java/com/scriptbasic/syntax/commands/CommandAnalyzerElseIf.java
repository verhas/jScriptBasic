package com.scriptbasic.syntax.commands;

import com.scriptbasic.context.Context;
import com.scriptbasic.executors.commands.CommandElseIf;
import com.scriptbasic.interfaces.AnalysisException;
import com.scriptbasic.interfaces.Command;
import com.scriptbasic.interfaces.Expression;

/**
 * @author Peter Verhas
 * date June 16, 2012
 */
public class CommandAnalyzerElseIf extends AbstractCommandAnalyzerIfKind {

    public CommandAnalyzerElseIf(final Context ctx) {
        super(ctx);
    }

    protected Command createNode(final Expression condition) throws AnalysisException {
        final CommandElseIf node = new CommandElseIf();
        node.setCondition(condition);
        registerAndSwapNode(node);
        return node;
    }

}

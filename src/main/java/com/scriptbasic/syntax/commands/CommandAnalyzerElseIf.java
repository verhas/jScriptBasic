package com.scriptbasic.syntax.commands;

import com.scriptbasic.context.Context;
import com.scriptbasic.executors.commands.CommandElseIf;
import com.scriptbasic.interfaces.AnalysisException;
import com.scriptbasic.spi.Command;

/**
 * @author Peter Verhas
 * date June 16, 2012
 */
public class CommandAnalyzerElseIf extends AbstractCommandAnalyzerIfKind {

    public CommandAnalyzerElseIf(final Context ctx) {
        super(ctx);
    }

    @Override
    public Command analyze() throws AnalysisException {

        final var condition = analyzeCondition();
        
        final var node = new CommandElseIf();
        node.setCondition(condition);
        registerAndSwapNode(node);
        consumeEndOfStatement();
        return node;
    }

}

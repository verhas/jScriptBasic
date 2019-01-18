package com.scriptbasic.syntax.commands;

import com.scriptbasic.context.Context;
import com.scriptbasic.executors.commands.CommandReturn;
import com.scriptbasic.interfaces.AnalysisException;
import com.scriptbasic.spi.Command;

/**
 * @author Peter Verhas
 * date Jul 12, 2012
 */
public class CommandAnalyzerReturn extends AbstractCommandAnalyzer {
    public CommandAnalyzerReturn(final Context ctx) {
        super(ctx);
    }

    /*
     * (non-Javadoc)
     *
     * @see com.scriptbasic.interfaces.Analyzer#analyze()
     */
    @Override
    public Command analyze() throws AnalysisException {
        final var node = new CommandReturn();
        final var le = ctx.lexicalAnalyzer.peek();
        if (le != null && !le.isLineTerminator()) {
            final var returnExpression = analyzeExpression();
            node.setReturnExpression(returnExpression);
        } else {
            node.setReturnExpression(null);
        }
        consumeEndOfLine();
        return node;
    }

}

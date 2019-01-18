package com.scriptbasic.syntax.commands;

import com.scriptbasic.context.Context;
import com.scriptbasic.executors.commands.CommandPrint;
import com.scriptbasic.interfaces.AnalysisException;
import com.scriptbasic.spi.Command;

/**
 * @author Peter Verhas
 * date Jul 12, 2012
 */
public class CommandAnalyzerPrint extends AbstractCommandAnalyzer {
    public CommandAnalyzerPrint(final Context ctx) {
        super(ctx);
    }

    /*
     * (non-Javadoc)
     *
     * @see com.scriptbasic.interfaces.Analyzer#analyze()
     */
    @Override
    public Command analyze() throws AnalysisException {
        final var node = new CommandPrint();
        final var expressionList = analyzeExpressionList();
        node.setExpressionList(expressionList);
        consumeEndOfLine();
        return node;
    }

}

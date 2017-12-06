package com.scriptbasic.syntax.commands;

import com.scriptbasic.context.Context;
import com.scriptbasic.executors.commands.CommandPrint;
import com.scriptbasic.interfaces.AnalysisException;
import com.scriptbasic.interfaces.Command;
import com.scriptbasic.interfaces.ExpressionList;

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
        final CommandPrint node = new CommandPrint();
        final ExpressionList expressionList = analyzeExpressionList();
        node.setExpressionList(expressionList);
        consumeEndOfLine();
        return node;
    }

}

package com.scriptbasic.syntax.commands;

import com.scriptbasic.executors.commands.CommandReturn;
import com.scriptbasic.factories.Context;
import com.scriptbasic.interfaces.AnalysisException;
import com.scriptbasic.interfaces.Command;
import com.scriptbasic.interfaces.Expression;
import com.scriptbasic.interfaces.LexicalElement;

/**
 * @author Peter Verhas
 * date Jul 12, 2012
 */
public class CommandAnalyzerReturn extends AbstractCommandAnalyzer {
    public CommandAnalyzerReturn(Context ctx) {
        super(ctx);
    }

    /*
         * (non-Javadoc)
         *
         * @see com.scriptbasic.interfaces.Analyzer#analyze()
         */
    @Override
    public Command analyze() throws AnalysisException {
        CommandReturn node = new CommandReturn();
        LexicalElement le = ctx.lexicalAnalyzer.peek();
        if (le != null && !le.isLineTerminator()) {
            Expression returnExpression = analyzeExpression();
            node.setReturnExpression(returnExpression);
        } else {
            node.setReturnExpression(null);
        }
        consumeEndOfLine();
        return node;
    }

}

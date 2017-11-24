package com.scriptbasic.syntax.commands;

import com.scriptbasic.executors.commands.CommandReturn;
import com.scriptbasic.interfaces.AnalysisException;
import com.scriptbasic.interfaces.Command;
import com.scriptbasic.interfaces.Expression;
import com.scriptbasic.interfaces.LexicalElement;
import com.scriptbasic.utility.FactoryUtility;

/**
 * @author Peter Verhas
 * date Jul 12, 2012
 * 
 */
public class CommandAnalyzerReturn extends AbstractCommandAnalyzer {
    /*
     * (non-Javadoc)
     * 
     * @see com.scriptbasic.interfaces.Analyzer#analyze()
     */
    @Override
    public Command analyze() throws AnalysisException {
        CommandReturn node = new CommandReturn();
        LexicalElement le = FactoryUtility.getLexicalAnalyzer(getFactory()).peek();
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

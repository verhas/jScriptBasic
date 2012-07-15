/**
 * 
 */
package com.scriptbasic.syntax.commands;

import com.scriptbasic.executors.commands.CommandReturn;
import com.scriptbasic.interfaces.AnalysisException;
import com.scriptbasic.interfaces.Command;
import com.scriptbasic.interfaces.Expression;

/**
 * @author Peter Verhas
 * @date Jul 12, 2012
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
        Expression returnExpression = analyzeExpression();
        node.setReturnExpression(returnExpression);
        consumeEndOfLine();
        return node;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.scriptbasic.syntax.commandanalyzers.AbstractCommandAnalyzer#getName()
     */
    @Override
    protected String getName() {
        return "RETURN";
    }

}

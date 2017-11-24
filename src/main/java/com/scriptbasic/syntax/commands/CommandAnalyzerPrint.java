package com.scriptbasic.syntax.commands;

import com.scriptbasic.executors.commands.CommandPrint;
import com.scriptbasic.interfaces.AnalysisException;
import com.scriptbasic.interfaces.Command;
import com.scriptbasic.interfaces.ExpressionList;

/**
 * @author Peter Verhas
 * date Jul 12, 2012
 * 
 */
public class CommandAnalyzerPrint extends AbstractCommandAnalyzer {
    /*
     * (non-Javadoc)
     * 
     * @see com.scriptbasic.interfaces.Analyzer#analyze()
     */
    @Override
    public Command analyze() throws AnalysisException {
        CommandPrint node = new CommandPrint();
        ExpressionList expressionList = analyzeExpressionList();
        node.setExpressionList(expressionList);
        consumeEndOfLine();
        return node;
    }

}

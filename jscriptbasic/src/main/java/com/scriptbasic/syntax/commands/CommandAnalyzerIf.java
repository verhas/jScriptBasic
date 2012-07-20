/**
 * 
 */
package com.scriptbasic.syntax.commands;

import com.scriptbasic.executors.commands.CommandIf;
import com.scriptbasic.interfaces.AnalysisException;
import com.scriptbasic.interfaces.Command;
import com.scriptbasic.interfaces.Expression;

/**
 * @author Peter Verhas
 * @date June 16, 2012
 * 
 */
public class CommandAnalyzerIf extends AbstractCommandAnalyzerIfKind {

//    protected void handleNode(CommandIf node) throws AnalysisException {
//        pushNode(node);
//    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.scriptbasic.syntax.commandanalyzers.AbstractCommandAnalyzer#getName()
     */
    @Override
    protected String getName() {
        return "IF";
    }

    protected Command createNode(Expression condition) throws AnalysisException {
        CommandIf node = new CommandIf();
        node.setCondition(condition);
        pushNode(node);
        return node;
    }
}

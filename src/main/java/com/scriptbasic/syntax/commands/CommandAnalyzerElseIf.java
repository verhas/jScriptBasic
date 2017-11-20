/**
 * 
 */
package com.scriptbasic.syntax.commands;

import com.scriptbasic.executors.commands.CommandElseIf;
import com.scriptbasic.interfaces.AnalysisException;
import com.scriptbasic.interfaces.Command;
import com.scriptbasic.interfaces.Expression;

/**
 * @author Peter Verhas
 * date June 16, 2012
 * 
 */
public class CommandAnalyzerElseIf extends AbstractCommandAnalyzerIfKind {

    protected Command createNode(Expression condition) throws AnalysisException {
        CommandElseIf node = new CommandElseIf();
        node.setCondition(condition);
        registerAndSwapNode(node);
        return node;
    }

}

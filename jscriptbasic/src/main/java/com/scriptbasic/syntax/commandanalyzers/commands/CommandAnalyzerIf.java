/**
 * 
 */
package com.scriptbasic.syntax.commandanalyzers.commands;

import com.scriptbasic.exceptions.AnalysisException;
import com.scriptbasic.executors.commands.CommandIf;
import com.scriptbasic.interfaces.Command;
import com.scriptbasic.interfaces.Expression;
import com.scriptbasic.syntax.commandanalyzers.AbstractCommandAnalyzer;

/**
 * @author Peter Verhas
 * @date June 16, 2012
 * 
 */
public class CommandAnalyzerIf extends AbstractCommandAnalyzer {

    /*
     * (non-Javadoc)
     * 
     * @see com.scriptbasic.interfaces.Analyzer#analyze()
     */
    @Override
    public Command analyze() throws AnalysisException {
        CommandIf node = new CommandIf();
        //
        Expression condition = analyzeExpression();
        assertKeyWord("THEN");
        assertThereAreNoSuperflouosCharactersOnTheLine();
        //
        node.setCondition(condition);
        pushNodeOnTheAnalysisStack(node);
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
        return "IF";
    }

}

/**
 * 
 */
package com.scriptbasic.syntax.commands;

import com.scriptbasic.exceptions.GenericSyntaxException;
import com.scriptbasic.executors.commands.CommandFor;
import com.scriptbasic.executors.commands.CommandNext;
import com.scriptbasic.interfaces.AnalysisException;
import com.scriptbasic.interfaces.Command;
import com.scriptbasic.interfaces.LeftValue;
import com.scriptbasic.interfaces.LexicalAnalyzer;
import com.scriptbasic.interfaces.LexicalElement;
import com.scriptbasic.interfaces.NestedStructureHouseKeeper;
import com.scriptbasic.utility.FactoryUtility;

/**
 * @author Peter Verhas
 * @date June 16, 2012
 * 
 */
public class CommandAnalyzerNext extends AbstractCommandAnalyzer {

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.scriptbasic.syntax.commandanalyzers.AbstractCommandAnalyzer#getName()
     */
    @Override
    protected String getName() {
        return "NEXT";
    }

    @Override
    public Command analyze() throws AnalysisException {
        CommandNext node = new CommandNext();
        LexicalAnalyzer lexicalAnalyzer = FactoryUtility
                .getLexicalAnalyzer(getFactory());
        LexicalElement lexicalElement = lexicalAnalyzer.peek();
        NestedStructureHouseKeeper nshk = FactoryUtility
                .getNestedStructureHouseKeeper(getFactory());
        CommandFor commandFor = nshk.pop(CommandFor.class);
        commandFor.setLoopEndNode(node);
        node.setLoopStartNode(commandFor);
        if (lexicalElement != null && !lexicalElement.isLineTerminator()) {
            LeftValue loopVariableB = analyzeSimpleLeftValue();
            LeftValue loopVariableA = commandFor.getLoopVariable();
            if (!equal(loopVariableA, loopVariableB)) {
                throw new GenericSyntaxException(
                        "The variable following the command "
                                + getName()
                                + " does not match the loop variable of the corresponging command FOR",
                        lexicalElement);
            }
        }
        consumeEndOfLine();
        return node;
    }
}

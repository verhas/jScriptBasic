package com.scriptbasic.syntax.commands;

import com.scriptbasic.executors.commands.CommandFor;
import com.scriptbasic.interfaces.AnalysisException;
import com.scriptbasic.interfaces.Command;
import com.scriptbasic.interfaces.LexicalAnalyzer;
import com.scriptbasic.utility.FactoryUtility;

/**
 * @author Peter Verhas
 * date June 16, 2012
 * 
 */
public class CommandAnalyzerFor extends AbstractCommandAnalyzer {

    @Override
    public Command analyze() throws AnalysisException {
        CommandFor node = new CommandFor();
        LexicalAnalyzer lexicalAnalyzer = FactoryUtility
                .getLexicalAnalyzer(getFactory());
        node.setLoopVariable(analyzeSimpleLeftValue());
        assertKeyWord("=");
        node.setLoopStartValue(analyzeExpression());
        assertKeyWord("to");
        node.setLoopEndValue(analyzeExpression());
        if (isKeyWord("step")) {
            lexicalAnalyzer.get();
            node.setLoopStepValue(analyzeExpression());
        } else {
            node.setLoopStepValue(null);
        }
        pushNode(node);
        consumeEndOfLine();
        return node;
    }

}

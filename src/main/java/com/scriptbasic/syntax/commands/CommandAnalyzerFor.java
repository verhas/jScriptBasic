package com.scriptbasic.syntax.commands;

import com.scriptbasic.executors.commands.CommandFor;
import com.scriptbasic.factories.Context;
import com.scriptbasic.interfaces.AnalysisException;
import com.scriptbasic.interfaces.Command;

/**
 * @author Peter Verhas
 * date June 16, 2012
 */
public class CommandAnalyzerFor extends AbstractCommandAnalyzer {

    public CommandAnalyzerFor(Context ctx) {
        super(ctx);
    }

    @Override
    public Command analyze() throws AnalysisException {
        CommandFor node = new CommandFor();
        node.setLoopVariable(analyzeSimpleLeftValue());
        assertKeyWord("=");
        node.setLoopStartValue(analyzeExpression());
        assertKeyWord("to");
        node.setLoopEndValue(analyzeExpression());
        if (isKeyWord("step")) {
            ctx.lexicalAnalyzer.get();
            node.setLoopStepValue(analyzeExpression());
        } else {
            node.setLoopStepValue(null);
        }
        pushNode(node);
        consumeEndOfLine();
        return node;
    }

}

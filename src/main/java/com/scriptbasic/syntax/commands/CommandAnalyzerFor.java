package com.scriptbasic.syntax.commands;

import com.scriptbasic.context.Context;
import com.scriptbasic.executors.commands.CommandFor;
import com.scriptbasic.interfaces.AnalysisException;
import com.scriptbasic.interfaces.ScriptBasicKeyWords;
import com.scriptbasic.spi.Command;

/**
 * @author Peter Verhas
 * date June 16, 2012
 */
public class CommandAnalyzerFor extends AbstractCommandAnalyzer {

    public CommandAnalyzerFor(final Context ctx) {
        super(ctx);
    }

    @Override
    public Command analyze() throws AnalysisException {
        final var node = new CommandFor();
        node.setLoopVariable(analyzeSimpleLeftValue());
        assertKeyWord("=");
        node.setLoopStartValue(analyzeExpression());
        assertKeyWord(ScriptBasicKeyWords.KEYWORD_TO);
        node.setLoopEndValue(analyzeExpression());
        if (isKeyWord(ScriptBasicKeyWords.KEYWORD_STEP)) {
            ctx.lexicalAnalyzer.get();
            node.setLoopStepValue(analyzeExpression());
        } else {
            node.setLoopStepValue(null);
        }
        pushNode(node);
        consumeEndOfStatement();
        return node;
    }

}

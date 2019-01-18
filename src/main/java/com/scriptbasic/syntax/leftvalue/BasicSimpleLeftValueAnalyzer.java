package com.scriptbasic.syntax.leftvalue;

import com.scriptbasic.context.Context;
import com.scriptbasic.executors.leftvalues.BasicLeftValue;
import com.scriptbasic.interfaces.AnalysisException;
import com.scriptbasic.interfaces.BasicSyntaxException;
import com.scriptbasic.interfaces.LeftValueAnalyzer;
import com.scriptbasic.interfaces.SimpleLeftValueAnalyzer;
import com.scriptbasic.spi.LeftValue;

/**
 * Simple Left value is defined as
 * <pre>
 * SIMPLE LEFTVALUE ::= identifier
 * </pre>
 *
 * @author Peter Verhas date July 15, 2012
 */
public class BasicSimpleLeftValueAnalyzer implements LeftValueAnalyzer,
        SimpleLeftValueAnalyzer {

    private final Context ctx;

    public BasicSimpleLeftValueAnalyzer(final Context ctx) {
        this.ctx = ctx;
    }

    @Override
    public LeftValue analyze() throws AnalysisException {
        final BasicLeftValue leftValue;
        final var lexicalElement = ctx.lexicalAnalyzer.peek();
        if (lexicalElement != null && lexicalElement.isIdentifier()) {
            ctx.lexicalAnalyzer.get();
            leftValue = new BasicLeftValue();
            leftValue.setIdentifier(lexicalElement.getLexeme());
        } else {
            throw new BasicSyntaxException(
                    "left value should start with an identifier",
                    lexicalElement, null);
        }
        return leftValue;
    }
}

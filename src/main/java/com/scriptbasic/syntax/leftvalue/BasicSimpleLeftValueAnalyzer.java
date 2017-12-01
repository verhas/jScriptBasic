package com.scriptbasic.syntax.leftvalue;

import com.scriptbasic.executors.leftvalues.BasicLeftValue;
import com.scriptbasic.factories.Context;
import com.scriptbasic.interfaces.*;

/**
 * Simple Left value is defined as
 * <p>
 * <pre>
 * SIMPLE LEFTVALUE ::= identifier
 * </pre>
 *
 * @author Peter Verhas date July 15, 2012
 */
public class BasicSimpleLeftValueAnalyzer implements LeftValueAnalyzer,
        SimpleLeftValueAnalyzer {

    private final Context ctx;

    public BasicSimpleLeftValueAnalyzer(Context ctx) {
        this.ctx = ctx;
    }

    @Override
    public LeftValue analyze() throws AnalysisException {
        BasicLeftValue leftValue = null;
        LexicalElement lexicalElement = ctx.lexicalAnalyzer.peek();
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

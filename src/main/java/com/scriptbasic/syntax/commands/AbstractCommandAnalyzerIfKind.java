package com.scriptbasic.syntax.commands;

import com.scriptbasic.context.Context;
import com.scriptbasic.interfaces.AnalysisException;
import com.scriptbasic.interfaces.Expression;
import com.scriptbasic.interfaces.ScriptBasicKeyWords;
import com.scriptbasic.spi.Command;

/**
 * @author Peter Verhas
 * date Jul 13, 2012
 */
public abstract class AbstractCommandAnalyzerIfKind extends
        AbstractCommandAnalyzerIfElseKind {
    public AbstractCommandAnalyzerIfKind(final Context ctx) {
        super(ctx);
    }

    protected abstract Command createNode(Expression condition) throws AnalysisException;

    /*
     * (non-Javadoc)
     *
     * @see com.scriptbasic.interfaces.Analyzer#analyze()
     */
    @Override
    public Command analyze() throws AnalysisException {
        return createNode(analizeLine());
    }

    protected Expression analizeLine() throws AnalysisException {
        final var condition = analyzeExpression();
        assertKeyWord(ScriptBasicKeyWords.KEYWORD_THEN);
        consumeEndOfLine();
        return condition;
    }

}

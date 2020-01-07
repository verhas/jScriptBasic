package com.scriptbasic.syntax.commands;

import com.scriptbasic.context.Context;
import com.scriptbasic.interfaces.AnalysisException;
import com.scriptbasic.interfaces.Expression;
import com.scriptbasic.interfaces.ScriptBasicKeyWords;

/**
 * @author Peter Verhas
 * date Jul 13, 2012
 */
public abstract class AbstractCommandAnalyzerIfKind extends
        AbstractCommandAnalyzerIfElseKind {
    public AbstractCommandAnalyzerIfKind(final Context ctx) {
        super(ctx);
    }

    /**
     * Analyse expression and THEN keyword
     * @return expression for IF statement
     * @throws AnalysisException error when missing then keyword or failed to parse expression
     */
    protected Expression analyzeCondition() throws AnalysisException {
        final var condition = analyzeExpression();
        assertKeyWord(ScriptBasicKeyWords.KEYWORD_THEN);
        return condition;
    }

}

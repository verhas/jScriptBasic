package com.scriptbasic.syntax.commands;

import com.scriptbasic.context.Context;
import com.scriptbasic.interfaces.AnalysisException;
import com.scriptbasic.api.Command;
import com.scriptbasic.interfaces.Expression;

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
        final Expression condition = analyzeExpression();
        assertKeyWord("THEN");
        consumeEndOfLine();
        return condition;
    }

}

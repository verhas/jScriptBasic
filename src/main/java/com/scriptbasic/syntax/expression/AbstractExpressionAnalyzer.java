package com.scriptbasic.syntax.expression;

import com.scriptbasic.context.Context;
import com.scriptbasic.errors.BasicInterpreterInternalError;
import com.scriptbasic.executors.operators.AbstractBinaryOperator;
import com.scriptbasic.interfaces.*;
import com.scriptbasic.syntax.AbstractAnalyzer;

import java.lang.reflect.InvocationTargetException;
import java.util.Map;

public abstract class AbstractExpressionAnalyzer extends
        AbstractAnalyzer<Expression> implements ExpressionAnalyzer {

    private final Context ctx;

    protected AbstractExpressionAnalyzer(final Context ctx) {
        this.ctx = ctx;
    }

    protected abstract Integer getMaximumPriority();

    protected abstract Map<String, Class<? extends AbstractBinaryOperator>> getOperatorMap(
            Integer priority);

    @Override
    public Expression analyze() throws AnalysisException {
        return analyze(getMaximumPriority());
    }

    private LexicalElement peekAtOperatorLexeme() throws AnalysisException {
        return ctx.lexicalAnalyzer.peek();
    }

    private void consumeTheOperatorLexeme() throws AnalysisException {
        ctx.lexicalAnalyzer.get();
    }

    private boolean isOperatorWithPriority(final LexicalElement le,
                                           final Integer priority) {
        return le != null && le.isSymbol()
                && getOperatorMap(priority).containsKey(le.getLexeme());
    }

    private Class<? extends AbstractBinaryOperator> getOperatorClass(
            final LexicalElement le, final Integer priority) {
        return getOperatorMap(priority).get(le.getLexeme());
    }

    private AbstractBinaryOperator getOperator(final LexicalElement le, final Integer priority)
            throws NoSuchMethodException,
            IllegalAccessException,
            InvocationTargetException,
            InstantiationException {
        return getOperatorClass(le, priority).getDeclaredConstructor().newInstance();
    }

    private Expression analyzeWithPositivePriority(final Integer priority)
            throws AnalysisException {
        final Expression expression;
        Expression leftOperand = analyze(priority - 1);
        try {
            for (; ; ) {
                final var le = peekAtOperatorLexeme();
                if (isOperatorWithPriority(le, priority)) {
                    consumeTheOperatorLexeme();
                    final var rightOperand = analyze(priority - 1);
                    final var operator = getOperator(le, priority);
                    operator.setLeftOperand(leftOperand);
                    operator.setRightOperand(rightOperand);
                    leftOperand = operator;
                } else {
                    expression = leftOperand;
                    break;
                }
            }
        } catch (final AnalysisException e) {
            throw new BasicSyntaxException(e);
        } catch (final Exception e) {
            throw new BasicInterpreterInternalError(
                    "Can not instantiate the operator class", e);
        }
        return expression;
    }

    /**
     * Analyze an expression that can contain operators on the top level of the
     * expression not higher than {@code priority}. Operators in sub expressions
     * enclosed between parentheses can however be of any priority.
     *
     * @param priority the maximum priority of operators in the expression
     * @return the expression that was analyzed
     * @throws AnalysisException when the expression has syntax error
     */
    private Expression analyze(final Integer priority) throws AnalysisException {
        if (priority == 0) {
            return ctx.tagAnalyzer.analyze();
        } else {
            return analyzeWithPositivePriority(priority);
        }
    }

}

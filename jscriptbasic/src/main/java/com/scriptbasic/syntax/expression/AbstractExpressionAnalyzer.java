package com.scriptbasic.syntax.expression;
import java.util.Map;
import com.scriptbasic.errors.BasicInterpreterInternalError;
import com.scriptbasic.exceptions.GenericSyntaxException;
import com.scriptbasic.exceptions.LexicalException;
import com.scriptbasic.executors.operators.AbstractBinaryOperator;
import com.scriptbasic.interfaces.AnalysisException;
import com.scriptbasic.interfaces.Expression;
import com.scriptbasic.interfaces.ExpressionAnalyzer;
import com.scriptbasic.interfaces.Factory;
import com.scriptbasic.interfaces.LexicalElement;
import com.scriptbasic.syntax.AbstractAnalyzer;
import com.scriptbasic.utility.FactoryUtility;
public abstract class AbstractExpressionAnalyzer extends
        AbstractAnalyzer<Expression> implements ExpressionAnalyzer {
    private Factory factory;
    @Override
    public Factory getFactory() {
        return factory;
    }
    @Override
    public void setFactory(Factory factory) {
        this.factory = factory;
    }
    protected abstract Integer getMaximumPriority();
    protected abstract Map<String, Class<? extends AbstractBinaryOperator>> getOperatorMap(
            Integer priority);
    @Override
    public Expression analyze() throws AnalysisException {
        return analyze(getMaximumPriority());
    }
    private LexicalElement peekAtOperatorLexeme() throws AnalysisException {
        return FactoryUtility.getLexicalAnalyzer(factory).peek();
    }
    private LexicalElement consumeTheOperatorLexeme() throws AnalysisException {
        return FactoryUtility.getLexicalAnalyzer(factory).get();
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
    private Expression analyzeWithPositivePriority(final Integer priority)
            throws AnalysisException {
        Expression expression = null;
        Expression leftOperand = analyze(priority - 1);
        try {
            for (;;) {
                final LexicalElement le = peekAtOperatorLexeme();
                if (isOperatorWithPriority(le, priority)) {
                    consumeTheOperatorLexeme();
                    final Expression rightOperand = analyze(priority - 1);
                    final AbstractBinaryOperator operator = getOperatorClass(
                            le, priority).newInstance();
                    operator.setLeftOperand(leftOperand);
                    operator.setRightOperand(rightOperand);
                    leftOperand = operator;
                } else {
                    expression = leftOperand;
                    break;
                }
            }
        } catch (final LexicalException e) {
            throw new GenericSyntaxException(e);
        } catch (final InstantiationException | IllegalAccessException e) {
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
     * @param priority
     * @return
     * @throws AnalysisException
     */
    private Expression analyze(final Integer priority) throws AnalysisException {
        if (priority == 0) {
            return FactoryUtility.getTagAnalyzer(factory).analyze();
        } else {
            return analyzeWithPositivePriority(priority);
        }
    }
}
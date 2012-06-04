package com.scriptbasic.syntax.expression;

import java.util.Map;

import com.scriptbasic.executors.operators.AbstractBinaryOperator;
import com.scriptbasic.interfaces.Expression;
import com.scriptbasic.interfaces.LexicalElement;
import com.scriptbasic.interfaces.LexicalException;
import com.scriptbasic.interfaces.SyntaxException;
import com.scriptbasic.syntax.AbstractAnalyzer;
import com.scriptbasic.syntax.GenericSyntaxException;

public abstract class AbstractExpressionAnalyzer extends AbstractAnalyzer {

    protected abstract Integer getMaximumPriority();

    protected abstract TagAnalyzer getTagAnalyzer();

    protected abstract Map<String, Class<? extends AbstractBinaryOperator>> getOperatorMap(
            Integer priority);

    @Override
    public Expression analyze() throws SyntaxException {
        return analyze(getMaximumPriority());
    }

    /**
     * Analyze an expression that can contain operators on the top level of the
     * expression not higher than {@code priority}. Operators in sub expressions
     * enclosed between parentheses can however be of any priority.
     * 
     * @param priority
     * @return
     * @throws SyntaxException
     */
    private Expression analyze(final Integer priority) throws SyntaxException {
        Expression expression = null;
        if (priority == 0) {
            expression = getTagAnalyzer().analyze();
        } else {
            final Expression leftOperand = analyze(priority - 1);
            try {
                final LexicalElement le = getSyntaxAnalyzer()
                        .getLexicalAnalyzer().peek();
                final Map<String, Class<? extends AbstractBinaryOperator>> map = getOperatorMap(priority);
                if (le != null && le.isSymbol() && map.containsKey(le.get())) {
                    getSyntaxAnalyzer().getLexicalAnalyzer().get();
                    final Expression rightOperand = analyze(priority - 1);
                    final AbstractBinaryOperator operator = map.get(le.get())
                            .newInstance();
                    operator.setLeftOperand(leftOperand);
                    operator.setRightOperand(rightOperand);
                    expression = operator;

                }else{
                    expression = leftOperand;
                }
            } catch (final LexicalException e) {
                throw new GenericSyntaxException(e);
            } catch (final InstantiationException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (final IllegalAccessException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        return expression;
    }

}

package com.scriptbasic.syntax.expression;

import java.util.Map;

import com.scriptbasic.command.executors.AbstractOperator;
import com.scriptbasic.command.executors.AbstractValue;
import com.scriptbasic.command.executors.BasicExpression;
import com.scriptbasic.interfaces.Expression;
import com.scriptbasic.interfaces.LexicalElement;
import com.scriptbasic.interfaces.LexicalException;
import com.scriptbasic.interfaces.SyntaxException;
import com.scriptbasic.syntax.AbstractAnalyzer;
import com.scriptbasic.syntax.GenericSyntaxException;

public abstract class AbstractExpressionAnalyzer extends AbstractAnalyzer {
    private BasicExpression expression;

    public AbstractValue getResult() {
        return expression;
    }

    protected abstract Integer getMaximumPriority();

    protected abstract TagAnalyzer getTagAnalyzer();

    protected abstract Map<String,Class<? extends AbstractOperator>> getOperatorMap(
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
    private Expression analyze(Integer priority) throws SyntaxException {
        Expression expression = null;
        if (priority == 1) {
            expression = getTagAnalyzer().analyze();
        } else {
            Expression leftOperand = analyze(priority - 1);
            try {
                LexicalElement le = getSyntaxAnalyzer().getLexicalAnalyzer()
                        .peek();
                if (le.isSymbol()) {
                    Map<String, Class<? extends AbstractOperator>> map = getOperatorMap(priority);
                    if (map.containsKey(le.get())) {
                        getSyntaxAnalyzer().getLexicalAnalyzer().get();
                        Expression rightOperand = analyze(priority - 1);
                        AbstractOperator operator = map.get(le.get()).newInstance();
                        operator.setLeftOperand(leftOperand);
                        operator.setRightOperand(rightOperand);
                        expression = operator;
                    }
                }
            } catch (LexicalException e) {
                throw new GenericSyntaxException(e);
            } catch (InstantiationException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        return expression;
    }

}

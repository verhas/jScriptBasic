package com.scriptbasic.syntax.expression;

import com.scriptbasic.command.executors.AbstractValue;
import com.scriptbasic.command.executors.BasicExpression;
import com.scriptbasic.interfaces.Analyzer;
import com.scriptbasic.interfaces.Expression;
import com.scriptbasic.interfaces.SyntaxException;

public abstract class ExpressionAnalyzer implements Analyzer {
    private BasicExpression expression;

    public AbstractValue getResult() {
        return expression;
    }

    protected abstract Integer getMaximumPriority();
    protected abstract TagAnalyzer getTagAnalyzer();
    
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
        if( priority == 1){
            expression = getTagAnalyzer().analyze();
        }else{
            
        }
        return expression;
    }

}

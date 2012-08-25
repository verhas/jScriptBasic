package com.scriptbasic.executors;

import com.scriptbasic.interfaces.ExpressionList;

public abstract class AbstractIdentifieredExpressionListedExpression extends
        AbstractIdentifieredExpression {

    private ExpressionList expressionList;

    public ExpressionList getExpressionList() {
        return this.expressionList;
    }

    public void setExpressionList(final ExpressionList expressionList) {
        this.expressionList = expressionList;
    }

}

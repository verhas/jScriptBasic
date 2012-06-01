package com.scriptbasic.executors.operators;

import com.scriptbasic.interfaces.Expression;

public abstract class AbstractBinaryOperator extends AbstractOperator {
    private Expression leftOperand;
    private Expression rightOperand;

    public Expression getLeftOperand() {
        return leftOperand;
    }

    public void setLeftOperand(final Expression leftOperand) {
        this.leftOperand = leftOperand;
    }

    public Expression getRightOperand() {
        return rightOperand;
    }

    public void setRightOperand(final Expression rightOperand) {
        this.rightOperand = rightOperand;
    }

}

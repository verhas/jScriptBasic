package com.scriptbasic.command.executors;

import com.scriptbasic.interfaces.Expression;

public abstract class AbstractOperator extends AbstractExpression {
    private Expression leftOperand;
    private Expression rightOperand;
    public Expression getLeftOperand() {
        return leftOperand;
    }
    public void setLeftOperand(Expression leftOperand) {
        this.leftOperand = leftOperand;
    }
    public Expression getRightOperand() {
        return rightOperand;
    }
    public void setRightOperand(Expression rightOperand) {
        this.rightOperand = rightOperand;
    }
}

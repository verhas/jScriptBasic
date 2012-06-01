package com.scriptbasic.executors.operators;

import com.scriptbasic.interfaces.Expression;

public abstract class AbstractUnaryOperator extends AbstractOperator {
    public Expression getOperand() {
        return operand;
    }

    public void setOperand(final Expression operand) {
        this.operand = operand;
    }

    private Expression operand;
    

}

package com.scriptbasic.executors.operators;

import com.scriptbasic.interfaces.BasicRuntimeException;
import com.scriptbasic.interfaces.Expression;
import com.scriptbasic.interfaces.RightValue;

public abstract class AbstractShortCircuitBinaryOperator extends
        AbstractBinaryOperator {

    protected abstract RightValue evaluateOn(RightValue leftOperand,
            Expression rightOperand) throws BasicRuntimeException;

    @Override
    public RightValue evaluate() throws BasicRuntimeException {
        return evaluateOn(getLeftOperand().evaluate(), getRightOperand());
    }

}

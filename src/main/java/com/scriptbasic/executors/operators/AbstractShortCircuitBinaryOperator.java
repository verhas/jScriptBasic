package com.scriptbasic.executors.operators;

import com.scriptbasic.interfaces.ExecutionException;
import com.scriptbasic.interfaces.Expression;
import com.scriptbasic.interfaces.Interpreter;
import com.scriptbasic.interfaces.RightValue;

public abstract class AbstractShortCircuitBinaryOperator extends
        AbstractBinaryOperator {

    protected abstract RightValue evaluateOn(
            Interpreter interpreter, RightValue leftOperand,
            Expression rightOperand) throws ExecutionException;

    @Override
    public RightValue evaluate(final Interpreter interpreter)
            throws ExecutionException {
        return evaluateOn(interpreter,
                getLeftOperand().evaluate(interpreter),
                getRightOperand());
    }

}

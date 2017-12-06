package com.scriptbasic.executors.operators;

import com.scriptbasic.interfaces.ExecutionException;
import com.scriptbasic.interfaces.Expression;
import com.scriptbasic.interfaces.Interpreter;
import com.scriptbasic.interfaces.RightValue;

public abstract class AbstractShortCircuitBinaryOperator extends
        AbstractBinaryOperator {

    protected abstract RightValue evaluateOn(
            Interpreter extendedInterpreter, RightValue leftOperand,
            Expression rightOperand) throws ExecutionException;

    @Override
    public RightValue evaluate(final Interpreter extendedInterpreter)
            throws ExecutionException {
        return evaluateOn(extendedInterpreter,
                getLeftOperand().evaluate(extendedInterpreter),
                getRightOperand());
    }

}

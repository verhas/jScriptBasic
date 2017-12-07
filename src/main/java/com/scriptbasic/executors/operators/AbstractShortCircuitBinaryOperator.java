package com.scriptbasic.executors.operators;

import com.scriptbasic.api.ScriptBasicException;
import com.scriptbasic.interfaces.Expression;
import com.scriptbasic.spi.Interpreter;
import com.scriptbasic.spi.RightValue;

public abstract class AbstractShortCircuitBinaryOperator extends
        AbstractBinaryOperator {

    protected abstract RightValue evaluateOn(
            Interpreter interpreter, RightValue leftOperand,
            Expression rightOperand) throws ScriptBasicException;

    @Override
    public RightValue evaluate(final Interpreter interpreter)
            throws ScriptBasicException {
        return evaluateOn(interpreter,
                getLeftOperand().evaluate(interpreter),
                getRightOperand());
    }

}

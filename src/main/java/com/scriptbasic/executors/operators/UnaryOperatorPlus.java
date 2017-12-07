package com.scriptbasic.executors.operators;

import com.scriptbasic.api.ScriptBasicException;
import com.scriptbasic.spi.Interpreter;
import com.scriptbasic.spi.RightValue;

public class UnaryOperatorPlus extends AbstractUnaryOperator {

    @Override
    public RightValue evaluate(final Interpreter interpreter)
            throws ScriptBasicException {
        return getOperand().evaluate(interpreter);
    }

}

package com.scriptbasic.executors.operators;

import com.scriptbasic.api.ScriptBasicException;
import com.scriptbasic.interfaces.Interpreter;
import com.scriptbasic.api.RightValue;

public class UnaryOperatorPlus extends AbstractUnaryOperator {

    @Override
    public RightValue evaluate(final Interpreter interpreter)
            throws ScriptBasicException {
        return getOperand().evaluate(interpreter);
    }

}

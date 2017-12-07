package com.scriptbasic.executors.operators;

import com.scriptbasic.api.ScriptBasicException;
import com.scriptbasic.executors.rightvalues.BasicBooleanValue;
import com.scriptbasic.spi.Interpreter;
import com.scriptbasic.spi.RightValue;

public class UnaryOperatorNot extends AbstractUnaryOperator {

    @Override
    public RightValue evaluate(final Interpreter interpreter)
            throws ScriptBasicException {
        new BasicBooleanValue(false);
        return new BasicBooleanValue(!BasicBooleanValue.asBoolean(getOperand()
                .evaluate(interpreter)));
    }
}

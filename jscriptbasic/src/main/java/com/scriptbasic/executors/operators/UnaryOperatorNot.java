package com.scriptbasic.executors.operators;

import com.scriptbasic.exceptions.BasicRuntimeException;
import com.scriptbasic.executors.rightvalues.BasicBooleanValue;
import com.scriptbasic.interfaces.RightValue;

public class UnaryOperatorNot extends AbstractUnaryOperator {

    @Override
    public RightValue evaluate() throws BasicRuntimeException {
        new BasicBooleanValue(false);
        return new BasicBooleanValue(!BasicBooleanValue.convert(getOperand()
                .evaluate()));
    }
}

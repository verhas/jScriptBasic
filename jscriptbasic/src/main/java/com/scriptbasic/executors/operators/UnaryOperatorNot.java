package com.scriptbasic.executors.operators;

import com.scriptbasic.executors.BasicBooleanValue;
import com.scriptbasic.interfaces.BasicRuntimeException;
import com.scriptbasic.interfaces.RightValue;

public class UnaryOperatorNot extends AbstractUnaryOperator {

    @Override
    public RightValue evaluate() throws BasicRuntimeException {
        new BasicBooleanValue(false);
        return new BasicBooleanValue(
                !BasicBooleanValue.convert(getOperand().evaluate()));
    }
}

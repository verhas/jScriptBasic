package com.scriptbasic.executors.operators;

import com.scriptbasic.exceptions.BasicRuntimeException;
import com.scriptbasic.interfaces.RightValue;

public class UnaryOperatorPlus extends AbstractUnaryOperator {

    @Override
    public RightValue evaluate() throws BasicRuntimeException {
        return getOperand().evaluate();
    }

}

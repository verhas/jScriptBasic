package com.scriptbasic.executors.operators;

import com.scriptbasic.interfaces.ExecutionException;
import com.scriptbasic.interfaces.ExtendedInterpreter;
import com.scriptbasic.interfaces.RightValue;

public class UnaryOperatorPlus extends AbstractUnaryOperator {

    @Override
    public RightValue evaluate(final ExtendedInterpreter extendedInterpreter)
            throws ExecutionException {
        return getOperand().evaluate(extendedInterpreter);
    }

}

package com.scriptbasic.executors.operators;

import com.scriptbasic.interfaces.BasicRuntimeException;
import com.scriptbasic.interfaces.RightValue;

public abstract class AbstractBinaryFullCircuitHalfDoubleOperator extends
        AbstractBinaryFullCircuitNumericOperator<Double> {

    @Override
    protected final RightValue operateOnDoubleLong(Double a, Long b)
            throws BasicRuntimeException {
        return operateOnDoubleDouble(a.doubleValue(), b.doubleValue());
    }

    @Override
    protected final RightValue operateOnLongDouble(Long a, Double b)
            throws BasicRuntimeException {
        return operateOnDoubleDouble(a.doubleValue(), b.doubleValue());
    }

}

package com.scriptbasic.executors.operators;

import com.scriptbasic.interfaces.BasicRuntimeException;
import com.scriptbasic.api.RightValue;

public abstract class AbstractBinaryFullCircuitHalfDoubleOperator extends
        AbstractBinaryFullCircuitNumericOperator<Double> {

    @Override
    protected final RightValue operateOnDoubleLong(final Double a, final Long b)
            throws BasicRuntimeException {
        return operateOnDoubleDouble(a, b.doubleValue());
    }

    @Override
    protected final RightValue operateOnLongDouble(final Long a, final Double b)
            throws BasicRuntimeException {
        return operateOnDoubleDouble(a.doubleValue(), b);
    }

}

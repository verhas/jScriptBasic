package com.scriptbasic.executors.operators;

import com.scriptbasic.spi.RightValue;

public abstract class AbstractBinaryFullCircuitHalfDoubleOperator extends
        AbstractBinaryFullCircuitNumericOperator<Double> {

    @Override
    protected final RightValue operateOnDoubleLong(final Double a, final Long b) {
        return operateOnDoubleDouble(a, b.doubleValue());
    }

    @Override
    protected final RightValue operateOnLongDouble(final Long a, final Double b) {
        return operateOnDoubleDouble(a.doubleValue(), b);
    }

}

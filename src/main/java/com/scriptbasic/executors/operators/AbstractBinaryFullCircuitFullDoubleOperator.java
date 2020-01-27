package com.scriptbasic.executors.operators;

import com.scriptbasic.spi.RightValue;

public abstract class AbstractBinaryFullCircuitFullDoubleOperator extends
        AbstractBinaryFullCircuitHalfDoubleOperator {

    @Override
    protected final RightValue operateOnLongLong(final Long a, final Long b) {
        return operateOnDoubleDouble(a.doubleValue(), b.doubleValue());
    }


    @Override
    protected RightValue operateOnDate(Long a, Long b) {
        return operateOnLongLong(a, b);
    }
}

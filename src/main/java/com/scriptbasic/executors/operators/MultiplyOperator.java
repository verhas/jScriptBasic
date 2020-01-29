package com.scriptbasic.executors.operators;

import com.scriptbasic.executors.rightvalues.BasicDoubleValue;
import com.scriptbasic.executors.rightvalues.BasicLongValue;
import com.scriptbasic.spi.RightValue;

public class MultiplyOperator extends
        AbstractBinaryFullCircuitHalfDoubleOperator {

    @Override
    protected RightValue operateOnDoubleDouble(final Double a, final Double b) {
        return new BasicDoubleValue(a * b);
    }

    @Override
    protected RightValue operateOnLongLong(final Long a, final Long b) {
        return new BasicLongValue(a * b);
    }

    @Override
    protected String operatorName() {
        return "Multiply";
    }

    @Override
    protected RightValue operateOnDate(Long a, Long b) {
        return operateOnLongLong(a, b);
    }

}

package com.scriptbasic.executors.operators;

import com.scriptbasic.executors.rightvalues.BasicLongValue;
import com.scriptbasic.spi.RightValue;

public class IntegerDivideOperator extends
        AbstractBinaryFullCircuitHalfDoubleOperator {

    @Override
    protected RightValue operateOnDoubleDouble(final Double a, final Double b) {
        return new BasicLongValue(Double.valueOf(a / b).longValue());
    }

    @Override
    protected RightValue operateOnLongLong(final Long a, final Long b) {
        return new BasicLongValue(a / b);
    }

    @Override
    protected String operatorName() {
        return "IntegerDivide";
    }

    @Override
    protected RightValue operateOnDate(Long a, Long b) {
        return operateOnLongLong(a, b);
    }

}

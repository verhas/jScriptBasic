package com.scriptbasic.executors.operators;

import com.scriptbasic.executors.rightvalues.BasicDateValue;
import com.scriptbasic.executors.rightvalues.BasicDoubleValue;
import com.scriptbasic.executors.rightvalues.BasicLongValue;
import com.scriptbasic.spi.RightValue;

public class MinusOperator extends AbstractBinaryFullCircuitHalfDoubleOperator {

    @Override
    protected RightValue operateOnDoubleDouble(final Double a, final Double b) {
        return new BasicDoubleValue(a - b);
    }

    @Override
    protected RightValue operateOnLongLong(final Long a, final Long b) {
        return new BasicLongValue(a - b);
    }

    @Override
    protected String operatorName() {
        return "Minus";
    }

    @Override
    protected RightValue operateOnDate(Long a, Long b) {
        return BasicDateValue.fromLong(a-b);
    }

}

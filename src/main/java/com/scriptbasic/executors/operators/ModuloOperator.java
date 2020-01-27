package com.scriptbasic.executors.operators;

import com.scriptbasic.executors.rightvalues.BasicDoubleValue;
import com.scriptbasic.executors.rightvalues.BasicLongValue;
import com.scriptbasic.spi.RightValue;

public class ModuloOperator extends
        AbstractBinaryFullCircuitNumericOperator<Number> {

    @Override
    protected RightValue operateOnDoubleDouble(final Double a, final Double b) {
        return new BasicDoubleValue(a % b);
    }

    @Override
    protected RightValue operateOnLongLong(final Long a, final Long b) {
        return new BasicLongValue(a % b);
    }

    @Override
    protected RightValue operateOnDoubleLong(final Double a, final Long b) {
        return new BasicDoubleValue(a % b);
    }

    @Override
    protected RightValue operateOnLongDouble(final Long a, final Double b) {
        return new BasicDoubleValue(a % b);
    }

    @Override
    protected String operatorName() {
        return "Modulo";
    }

    @Override
    protected RightValue operateOnDate(Long a, Long b) {
        return operateOnLongLong(a, b);
    }
}

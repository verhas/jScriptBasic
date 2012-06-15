package com.scriptbasic.executors.operators;

import com.scriptbasic.exceptions.BasicRuntimeException;
import com.scriptbasic.executors.rightvalues.BasicDoubleValue;
import com.scriptbasic.executors.rightvalues.BasicLongValue;
import com.scriptbasic.interfaces.RightValue;

public class ModuloOperator extends
        AbstractBinaryFullCircuitNumericOperator<Number> {

    @Override
    protected RightValue operateOnDoubleDouble(final Double a, final Double b)
            throws BasicRuntimeException {
        return new BasicDoubleValue(a % b);
    }

    @Override
    protected RightValue operateOnLongLong(final Long a, final Long b)
            throws BasicRuntimeException {
        return new BasicLongValue(a % b);
    }

    @Override
    protected RightValue operateOnDoubleLong(final Double a, final Long b)
            throws BasicRuntimeException {
        return new BasicDoubleValue(a % b);
    }

    @Override
    protected RightValue operateOnLongDouble(final Long a, final Double b)
            throws BasicRuntimeException {
        return new BasicDoubleValue(a % b);
    }

    @Override
    protected String operatorName() {
        return "Modulo";
    }
}

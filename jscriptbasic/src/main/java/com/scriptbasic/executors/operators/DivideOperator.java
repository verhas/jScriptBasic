package com.scriptbasic.executors.operators;

import com.scriptbasic.exceptions.BasicRuntimeException;
import com.scriptbasic.executors.rightvalues.BasicDoubleValue;
import com.scriptbasic.executors.rightvalues.BasicLongValue;
import com.scriptbasic.interfaces.RightValue;

public class DivideOperator extends AbstractBinaryFullCircuitHalfDoubleOperator {

    @Override
    protected RightValue operateOnDoubleDouble(final Double a, final Double b)
            throws BasicRuntimeException {
        return new BasicDoubleValue(a / b);
    }

    @Override
    protected RightValue operateOnLongLong(final Long a, final Long b)
            throws BasicRuntimeException {
        if (a % b == 0) {
            return new BasicLongValue(a / b);
        } else {
            return new BasicDoubleValue(a.doubleValue() / b.doubleValue());
        }
    }

    @Override
    protected String operatorName() {
        return "Divide";
    }

}

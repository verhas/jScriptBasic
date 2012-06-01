package com.scriptbasic.executors.operators;

import com.scriptbasic.executors.BasicDoubleValue;
import com.scriptbasic.executors.BasicLongValue;
import com.scriptbasic.interfaces.BasicRuntimeException;
import com.scriptbasic.interfaces.RightValue;

public class MultiplyOperator extends
        AbstractBinaryFullCircuitHalfDoubleOperator {

    protected RightValue operateOnDoubleDouble(Double a, Double b)
            throws BasicRuntimeException {
        return new BasicDoubleValue(a * b);
    }

    @Override
    protected RightValue operateOnLongLong(Long a, Long b)
            throws BasicRuntimeException {
        return new BasicLongValue(a * b);
    }

    @Override
    protected String operatorName() {
        return "Multiply";
    }

}

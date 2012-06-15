package com.scriptbasic.executors.operators;

import com.scriptbasic.exceptions.BasicRuntimeException;
import com.scriptbasic.executors.rightvalues.BasicDoubleValue;
import com.scriptbasic.interfaces.RightValue;

public class PowerOperator extends AbstractBinaryFullCircuitFullDoubleOperator {

    @Override
    protected RightValue operateOnDoubleDouble(final Double a, final Double b)
            throws BasicRuntimeException {
        return new BasicDoubleValue(Math.pow(a, b));
    }

    @Override
    protected String operatorName() {
        return "Power";
    }

}

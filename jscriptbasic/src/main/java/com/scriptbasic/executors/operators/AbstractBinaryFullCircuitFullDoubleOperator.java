package com.scriptbasic.executors.operators;

import com.scriptbasic.interfaces.BasicRuntimeException;
import com.scriptbasic.interfaces.RightValue;

public abstract class AbstractBinaryFullCircuitFullDoubleOperator extends
        AbstractBinaryFullCircuitHalfDoubleOperator {

    @Override
    protected final RightValue operateOnLongLong(Long a, Long b)
            throws BasicRuntimeException {
        return operateOnDoubleDouble(a.doubleValue(), b.doubleValue());
    }

}

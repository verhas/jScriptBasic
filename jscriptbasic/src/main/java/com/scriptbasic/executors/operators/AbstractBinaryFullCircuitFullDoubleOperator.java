package com.scriptbasic.executors.operators;
import com.scriptbasic.interfaces.BasicRuntimeException;
import com.scriptbasic.interfaces.RightValue;
public abstract class AbstractBinaryFullCircuitFullDoubleOperator extends
        AbstractBinaryFullCircuitHalfDoubleOperator {
    @Override
    protected final RightValue operateOnLongLong(final Long a, final Long b)
            throws BasicRuntimeException {
        return operateOnDoubleDouble(a.doubleValue(), b.doubleValue());
    }
}
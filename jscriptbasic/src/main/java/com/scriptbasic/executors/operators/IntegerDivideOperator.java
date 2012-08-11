package com.scriptbasic.executors.operators;
import com.scriptbasic.executors.rightvalues.BasicLongValue;
import com.scriptbasic.interfaces.BasicRuntimeException;
import com.scriptbasic.interfaces.RightValue;
public class IntegerDivideOperator extends
        AbstractBinaryFullCircuitHalfDoubleOperator {
    @Override
    protected RightValue operateOnDoubleDouble(final Double a, final Double b)
            throws BasicRuntimeException {
        return new BasicLongValue(new Double(a / b).longValue());
    }
    @Override
    protected RightValue operateOnLongLong(final Long a, final Long b)
            throws BasicRuntimeException {
        return new BasicLongValue(a / b);
    }
    @Override
    protected String operatorName() {
        return "IntegerDivide";
    }
}
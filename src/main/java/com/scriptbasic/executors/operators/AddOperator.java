package com.scriptbasic.executors.operators;

import com.scriptbasic.executors.rightvalues.*;
import com.scriptbasic.interfaces.BasicRuntimeException;
import com.scriptbasic.spi.RightValue;

public class AddOperator extends AbstractBinaryFullCircuitHalfDoubleOperator {

    private static String myToString(final Object o) {
        if (o == null)
            return "null";
        return o.toString();
    }

    private static String getString(final RightValue op)
            throws BasicRuntimeException {
        if (op.isString()) {
            return ((BasicStringValue) op).getValue();
        }
        if (op.isDouble()) {
            return myToString(((BasicDoubleValue) op).getValue());
        }
        if (op.isLong()) {
            return myToString(((BasicLongValue) op).getValue());
        }
        if (op.isBoolean()) {
            return myToString(((BasicBooleanValue) op).getValue());
        }
        if (op.isJavaObject()) {
            return myToString(((BasicJavaObjectValue) op).getValue());
        }
        throw new BasicRuntimeException(
                "Argument can not be converted to string");
    }

    @Override
    protected RightValue operateOnDoubleDouble(final Double a, final Double b) {
        return new BasicDoubleValue(a + b);
    }

    @Override
    protected RightValue operateOnLongLong(final Long a, final Long b) {
        return new BasicLongValue(a + b);
    }

    /**
     * If any operand is a string then concatenate the strings
     *
     * @param leftOperand  parameter
     * @param rightOperand parameter
     * @return the concatenated string
     * @throws BasicRuntimeException in case of exception
     */
    @Override
    protected RightValue operateOnValues(final RightValue leftOperand,
                                         final RightValue rightOperand) throws BasicRuntimeException {
        return new BasicStringValue(getString(leftOperand) + getString(rightOperand));
    }

    @Override
    protected String operatorName() {
        return "Plus";
    }

    @Override
    protected RightValue operateOnDate(Long a, Long b) {
        return BasicDateValue.fromLong(a+b);
    }

}

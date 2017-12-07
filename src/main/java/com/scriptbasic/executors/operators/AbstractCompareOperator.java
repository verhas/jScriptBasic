package com.scriptbasic.executors.operators;

import com.scriptbasic.executors.rightvalues.*;
import com.scriptbasic.interfaces.BasicRuntimeException;
import com.scriptbasic.spi.RightValue;

public abstract class AbstractCompareOperator extends
        AbstractBinaryFullCircuitOperator {

    protected static int compareJavaObjectTo(final BasicJavaObjectValue f,
                                             final RightValue op) throws BasicRuntimeException {
        final Object o = BasicJavaObjectValue.asObject(op);
        if (f.getValue() instanceof Comparable<?> && o instanceof Comparable<?>) {
            @SuppressWarnings("unchecked") final Comparable<Comparable<?>> a = (Comparable<Comparable<?>>) f
                    .getValue();
            final Comparable<?> b = (Comparable<?>) o;
            return a.compareTo(b);
        }
        throw new BasicRuntimeException(
                "Can not compare the java objects, at least one of them is not comparable");
    }

    protected abstract Boolean compareTo(BasicDoubleValue d, RightValue op)
            throws BasicRuntimeException;

    protected abstract Boolean compareTo(BasicLongValue l, RightValue op)
            throws BasicRuntimeException;

    protected abstract Boolean compareTo(BasicStringValue s, RightValue op)
            throws BasicRuntimeException;

    protected abstract Boolean compareTo(BasicJavaObjectValue s, RightValue op)
            throws BasicRuntimeException;

    protected abstract Boolean compareTo(BasicBooleanValue s, RightValue op)
            throws BasicRuntimeException;

    @Override
    protected RightValue evaluateOn(final RightValue leftOperand,
                                    final RightValue rightOperand) throws BasicRuntimeException {
        if (leftOperand.isDouble()) {
            return new BasicBooleanValue(compareTo(
                    ((BasicDoubleValue) leftOperand), rightOperand));
        }
        if (leftOperand.isLong()) {
            return new BasicBooleanValue(compareTo(
                    ((BasicLongValue) leftOperand), rightOperand));
        }
        if (leftOperand.isBoolean()) {
            return new BasicBooleanValue(compareTo(
                    ((BasicBooleanValue) leftOperand), rightOperand));
        }
        if (leftOperand.isString()) {
            return new BasicBooleanValue(compareTo(
                    ((BasicStringValue) leftOperand), rightOperand));
        }
        if (leftOperand.isJavaObject()) {
            return new BasicBooleanValue(compareTo(
                    ((BasicJavaObjectValue) leftOperand), rightOperand));
        }
        return null;
    }
}

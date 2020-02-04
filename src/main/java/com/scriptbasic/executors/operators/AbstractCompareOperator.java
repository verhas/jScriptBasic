package com.scriptbasic.executors.operators;

import com.scriptbasic.executors.rightvalues.*;
import com.scriptbasic.interfaces.BasicRuntimeException;
import com.scriptbasic.spi.BasicValue;
import com.scriptbasic.spi.RightValue;

public abstract class AbstractCompareOperator extends
        AbstractBinaryFullCircuitOperator {

    protected static int compareJavaObjectTo(final Object l,
                                             final Object r) throws BasicRuntimeException {
        if (l instanceof Comparable<?> && r instanceof Comparable<?>) {
            @SuppressWarnings("unchecked")
            final Comparable<Comparable<?>> a = (Comparable<Comparable<?>>) l;
            final Comparable<?> b = (Comparable<?>) r;
            return a.compareTo(b);
        }
        throw new BasicRuntimeException(
                "Can not compare the java objects, at least one of them is not comparable");
    }

    protected abstract Boolean compareTo(boolean l, boolean r)
            throws BasicRuntimeException;

    protected abstract Boolean compareTo(double l, double r)
            throws BasicRuntimeException;

    protected abstract Boolean compareTo(long l, long r)
            throws BasicRuntimeException;

    protected abstract Boolean compareTo(String l, String r)
            throws BasicRuntimeException;

    protected abstract Boolean compareTo(Object l, Object r)
            throws BasicRuntimeException;

    @Override
    protected RightValue evaluateOn(final RightValue leftOperand,
                                    final RightValue rightOperand) throws BasicRuntimeException {
        if (leftOperand == null && rightOperand == null) {
            return BasicValue.TRUE;
        }
        if (leftOperand == null || rightOperand == null) {
            return BasicValue.FALSE;
        }
        if (leftOperand.isLong() || rightOperand.isLong()) {
            final Long leftValue = getAsLong(leftOperand);
            final Long rightValue = getAsLong(rightOperand);
            if (leftValue != null && rightValue != null)
                return new BasicBooleanValue(compareTo(leftValue, rightValue));
        }
        if (leftOperand.isLong() || rightOperand.isLong() || leftOperand.isDouble() || rightOperand.isDouble()) {
            final Double leftValue = getAsDouble(leftOperand);
            final Double rightValue = getAsDouble(rightOperand);
            if (leftValue != null && rightValue != null)
                return new BasicBooleanValue(compareTo(leftValue, rightValue));
        }
        if (leftOperand.isBoolean() && rightOperand.isBoolean()) {
            final Boolean leftValue = getAsBoolean(leftOperand);
            final Boolean rightValue = getAsBoolean(rightOperand);
            if (leftValue != null && rightValue != null)
                return new BasicBooleanValue(compareTo(leftValue, rightValue));
        }
        if (leftOperand.isString() || rightOperand.isString()) {
            final String leftValue = getAsString(leftOperand);
            final String rightValue = getAsString(rightOperand);
            if (leftValue != null && rightValue != null)
                return new BasicBooleanValue(compareTo(leftValue, rightValue));
        }
        if (leftOperand.isJavaObject() || rightOperand.isJavaObject()) {
            final Object leftValue = getAsObject(leftOperand);
            final Object rightValue = getAsObject(rightOperand);
            if (leftValue != null && rightValue != null)
                return new BasicBooleanValue(compareTo(leftValue, rightValue));
        }
        throw new BasicRuntimeException("Type mismatch, left operand: " + leftOperand +
                ", right operand: " + rightOperand);
    }
}

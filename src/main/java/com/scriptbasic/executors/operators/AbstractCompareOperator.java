package com.scriptbasic.executors.operators;

import com.scriptbasic.executors.rightvalues.*;
import com.scriptbasic.interfaces.BasicRuntimeException;
import com.scriptbasic.spi.BasicValue;
import com.scriptbasic.spi.RightValue;

public abstract class AbstractCompareOperator extends
        AbstractBinaryFullCircuitOperator {

    /**
     * Get final decision from comparison result.
     * 
     * @param comparisonResult
     *            Result from function compareTo
     * @return final decision
     */
    abstract protected boolean decide(final int comparisonResult);

    protected <T> Boolean compareTo(Comparable<T> l, T r)
            throws BasicRuntimeException {
        final int comparisonResult = l.compareTo(r);
        return decide(comparisonResult);
    }

    @Override
    protected RightValue evaluateOn(final RightValue leftOperand,
                                    final RightValue rightOperand) throws BasicRuntimeException {
        if (leftOperand == null && rightOperand == null) {
            return BasicValue.TRUE;
        }
        if (leftOperand == null || rightOperand == null) {
            return BasicValue.FALSE;
        }
        if (leftOperand.isLong() || rightOperand.isLong() || leftOperand.isDate() || rightOperand.isDate()) {
            final Long leftValue = getAsLong(leftOperand);
            final Long rightValue = getAsLong(rightOperand);
            if (leftValue != null && rightValue != null)
                return new BasicBooleanValue(compareTo(leftValue, rightValue));
        }
        if (leftOperand.isLong() || rightOperand.isLong() || leftOperand.isDate() || rightOperand.isDate()
                || leftOperand.isDouble() || rightOperand.isDouble()) {
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
            if (leftValue != null && rightValue != null) {
                if (leftValue instanceof Comparable<?> && rightValue instanceof Comparable<?>) {
                    @SuppressWarnings("unchecked")
                    final Comparable<Comparable<?>> a = (Comparable<Comparable<?>>) leftValue;
                    final Comparable<?> b = (Comparable<?>) rightValue;
                    return new BasicBooleanValue(compareTo(a, b));
                }
            }
        }
        if (leftOperand == BasicEmptyValue.EMPTY_VALUE && rightOperand == BasicEmptyValue.EMPTY_VALUE) {
            return new BasicBooleanValue(compareTo(BasicEmptyValue.EMPTY_VALUE.getNumericValue(),
                                                   BasicEmptyValue.EMPTY_VALUE.getNumericValue()));
        }
        throw new BasicRuntimeException("Type mismatch, left operand: " + leftOperand +
                ", right operand: " + rightOperand);
    }
}

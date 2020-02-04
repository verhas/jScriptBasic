package com.scriptbasic.executors.rightvalues;

import com.scriptbasic.interfaces.BasicRuntimeException;
import com.scriptbasic.spi.RightValue;

public class BasicDoubleValue extends AbstractNumericRightValue<Double, Double> {

    public BasicDoubleValue(final Double d) {
        setValue(d);
    }

    public static Double asDouble(final RightValue rv, final String errorMessageForNull)
            throws BasicRuntimeException {
        final var value = asDouble(rv);
        if (value == null) {
            throw new BasicRuntimeException(errorMessageForNull);
        }
        return value;
    }

    public static Double asDouble(final RightValue rv)
            throws BasicRuntimeException {
        if (rv.isBoolean()) {
            return ((BasicBooleanValue) rv).getValue() ? 1.0 : 0.0;
        }
        if (rv.isString()) {
            final var s = ((BasicStringValue) rv).getValue();
            if (s == null) {
                return null;
            }
            try {
                return Double.parseDouble(s);
            } catch (NumberFormatException e) {
                throw new BasicRuntimeException("Can not convert value to double", e);
            }
        }
        if (rv.isLong()) {
            final var l = ((BasicLongValue) rv).getValue();
            if (l == null) {
                return null;
            }
            return l.doubleValue();
        }
        if (rv.isDate()) {
            return ((BasicDateValue) rv).getNumericValue().doubleValue();
        }
        if (rv.isDouble()) {
            return ((BasicDoubleValue) rv).getValue();
        }
        if (rv.isJavaObject()) {
            final var o = ((BasicJavaObjectValue) rv).getValue();
            if (o instanceof Double) {
                return (Double) o;
            }
            // TODO elaborate the conversion with other object classes, like
            // Long, String...
        }
        if (rv == BasicEmptyValue.EMPTY_VALUE) {
            return 0.0;
        }
        throw new BasicRuntimeException("Can not convert value to double");
    }

    @Override
    public String toString() {
        try {
            return "" + asDouble(this);
        } catch (final BasicRuntimeException e) {
            return super.toString();
        }
    }

    @Override
    public Double getNumericValue() {
        return getValue();
    }
}

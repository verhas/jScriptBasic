package com.scriptbasic.executors.rightvalues;

import com.scriptbasic.interfaces.BasicRuntimeException;
import com.scriptbasic.spi.RightValue;

public class BasicLongValue extends AbstractNumericRightValue<Long, Long> {

    public BasicLongValue(final Long i) {
        setValue(i);
    }

    public static Long asLong(final RightValue rv, final String errorMessageForNull)
            throws BasicRuntimeException {
        final var value = asLong(rv);
        if (value == null) {
            throw new BasicRuntimeException(errorMessageForNull);
        }
        return value;
    }

    public static Long asLong(final RightValue rv)
            throws BasicRuntimeException {
        if (rv.isBoolean()) {
            return ((BasicBooleanValue) rv).getValue() ? 1L : 0L;
        }
        if (rv.isString()) {
            final var s = ((BasicStringValue) rv).getValue();
            if (s == null) {
                return null;
            }
            try {
                return Long.parseLong(s);
            } catch (NumberFormatException e) {
                throw new BasicRuntimeException("Can not convert value to long", e);
            }
        }
        if (rv.isLong()) {
            return ((BasicLongValue) rv).getValue();
        }
        if (rv.isDouble()) {
            return ((BasicDoubleValue) rv).getValue().longValue();
        }
        if (rv.isJavaObject()) {
            final var o = ((BasicJavaObjectValue) rv).getValue();
            if (o instanceof Long) {
                return (Long) o;
            }
            // TODO elaborate the conversion with other object classes, like
            // Double, String...
        }
        if (rv == BasicEmptyValue.EMPTY_VALUE) {
            return 0L;
        }
        throw new BasicRuntimeException("Can not convert value to long");
    }

    @Override
    public String toString() {
        try {
            return "" + asLong(this);
        } catch (final BasicRuntimeException e) {
            return super.toString();
        }
    }

    @Override
    public Long getNumericValue() {
        return getValue();
    }
}

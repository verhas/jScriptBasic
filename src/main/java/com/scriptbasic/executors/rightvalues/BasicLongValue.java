package com.scriptbasic.executors.rightvalues;

import com.scriptbasic.interfaces.BasicRuntimeException;
import com.scriptbasic.spi.RightValue;

public class BasicLongValue extends AbstractNumericRightValue<Long> {

    public BasicLongValue(final Long i) {
        setValue(i);
    }

    public static Long asLong(final RightValue rightValue)
            throws BasicRuntimeException {
        if (rightValue.isBoolean()) {
            return ((BasicBooleanValue) rightValue).getValue() ? 1L : 0L;
        }
        if (rightValue.isString()) {
            final String s = ((BasicStringValue) rightValue).getValue();
            if (s == null) {
                return null;
            }
            return Long.parseLong(s);
        }
        if (rightValue.isLong()) {
            return ((BasicLongValue) rightValue).getValue();
        }
        if (rightValue.isDouble()) {
            return ((BasicDoubleValue) rightValue).getValue().longValue();
        }
        if (rightValue.isJavaObject()) {
            final Object o = ((BasicJavaObjectValue) rightValue).getValue();
            if (o instanceof Long) {
                return (Long) o;
            }
            // TODO elaborate the conversion with other object classes, like
            // Double, String...
        }
        throw new BasicRuntimeException("Can not convert value to long");
    }

    @Override
    public String toString() {
        try {
            return asLong(this).toString();
        } catch (BasicRuntimeException e) {
            return super.toString();
        }
    }
}

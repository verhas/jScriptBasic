package com.scriptbasic.executors.rightvalues;

import com.scriptbasic.interfaces.BasicRuntimeException;
import com.scriptbasic.interfaces.RightValue;

public class BasicLongValue extends AbstractNumericRightValue<Long> {

    public BasicLongValue(final Long i) {
        setValue(i);
    }

    public static Long asLong(final RightValue rv)
            throws BasicRuntimeException {
        if (rv.isBoolean()) {
            return ((BasicBooleanValue) rv).getValue() ? 1L : 0L;
        }
        if (rv.isString()) {
            final String s = ((BasicStringValue) rv).getValue();
            if (s == null) {
                return null;
            }
            return Long.parseLong(s);
        }
        if (rv.isLong()) {
            return ((BasicLongValue) rv).getValue();
        }
        if (rv.isDouble()) {
            return ((BasicDoubleValue) rv).getValue().longValue();
        }
        if (rv.isJavaObject()) {
            final Object o = ((BasicJavaObjectValue) rv).getValue();
            if (o instanceof Long) {
                return (Long) o;
            }
            // TODO elaborate the conversion with other object classes, like
            // Double, String...
        }
        throw new BasicRuntimeException("Can not convert value to long");
    }
}

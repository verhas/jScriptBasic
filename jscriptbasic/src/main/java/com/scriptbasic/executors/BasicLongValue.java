package com.scriptbasic.executors;

import com.scriptbasic.interfaces.BasicRuntimeException;
import com.scriptbasic.interfaces.RightValue;

public class BasicLongValue extends AbstractNumericRightValue<Long> {

    public BasicLongValue(final Long i) {
        setValue(i);
    }

    private BasicLongValue() {
    }

    private static BasicLongValue singleton = new BasicLongValue();
    public static Long convert(RightValue rv)throws BasicRuntimeException {
        return singleton.convertRightValue(rv);
    }

    @Override
    protected Long convertRightValue(RightValue rv) throws BasicRuntimeException {
        if (rv.isBoolean()) {
            return ((BasicBooleanValue) rv).getValue() ? 1L : 0L;
        }
        if (rv.isString()) {
            String s = ((BasicStringValue) rv).getValue();
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
            Object o = ((BasicJavaObjectValue) rv).getValue();
            if (o instanceof Long) {
                return (Long) o;
            }
            // TODO elaborate the conversion with other object classes, like
            // Double, String...
        }
        throw new BasicRuntimeException("Can not convert value to long");
    }
}

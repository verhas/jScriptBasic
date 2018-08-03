package com.scriptbasic.spi;

import com.scriptbasic.executors.rightvalues.*;

public interface BasicValue<T> {
    BasicBooleanValue TRUE = new BasicBooleanValue(true);
    BasicBooleanValue FALSE = new BasicBooleanValue(false);
    static RightValue create(final Object object) {
        if (object instanceof Boolean) {
            return new BasicBooleanValue((Boolean) object);
        }
        if (object instanceof Double) {
            return new BasicDoubleValue((Double) object);
        }
        if (object instanceof Float) {
            return new BasicDoubleValue((double) (float) object);
        }
        if (object instanceof Integer) {
            return new BasicLongValue((long) object);
        }
        if (object instanceof Long) {
            return new BasicLongValue((Long) object);
        }
        if (object instanceof Byte) {
            return new BasicLongValue((long) (byte) object);
        }
        if (object instanceof Character) {
            return new BasicLongValue((long) (char) object);
        }
        if (object instanceof String) {
            return new BasicStringValue((String) object);
        }
        return new BasicJavaObjectValue(object);
    }
    T getValue();
}

package com.scriptbasic.executors.rightvalues;

import com.scriptbasic.interfaces.BasicRuntimeException;
import com.scriptbasic.interfaces.RightValue;

public class BasicBooleanValue extends AbstractPrimitiveRightValue<Boolean> {
    public BasicBooleanValue(final Boolean b) {
        setValue(b);
    }

    public static Boolean convert(final RightValue rv)
            throws BasicRuntimeException {
        if (rv.isBoolean()) {
            return ((BasicBooleanValue) rv).getValue();
        }
        if (rv.isString()) {
            final String s = ((BasicStringValue) rv).getValue();
            return s != null && s.length() > 0;
        }
        if (rv.isLong()) {
            final Long l = ((BasicLongValue) rv).getValue();
            return l != null && l != 0;
        }
        if (rv.isDouble()) {
            final Double d = ((BasicDoubleValue) rv).getValue();
            return d != null && d != 0;
        }
        if (rv.isJavaObject()) {
            final Object o = ((BasicJavaObjectValue) rv).getValue();
            if (o instanceof Boolean) {
                return (Boolean) o;
            }
            // TODO extend the conversion in case of other simple types
            return o != null;
        }
        throw new BasicRuntimeException("Can not convert value to boolean");
    }
}

package com.scriptbasic.executors;

import com.scriptbasic.interfaces.BasicRuntimeException;
import com.scriptbasic.interfaces.RightValue;

public class BasicBooleanValue extends AbstractPrimitiveRightValue<Boolean> {
    public BasicBooleanValue(Boolean b) {
        setValue(b);
    }

    private BasicBooleanValue() {
    }

    private static BasicBooleanValue singleton = new BasicBooleanValue();

    public static Boolean convert(RightValue rv) throws BasicRuntimeException {
        return singleton.convertRightValue(rv);
    }
    
    @Override
    protected Boolean convertRightValue(RightValue rv) throws BasicRuntimeException {
        if (rv.isBoolean()) {
            return ((BasicBooleanValue) rv).getValue();
        }
        if (rv.isString()) {
            String s = ((BasicStringValue) rv).getValue();
            return s != null && s.length() > 0;
        }
        if (rv.isLong()) {
            Long l = ((BasicLongValue) rv).getValue();
            return l != null && l != 0;
        }
        if (rv.isDouble()) {
            Double d = ((BasicDoubleValue) rv).getValue();
            return d != null && d != 0;
        }
        if (rv.isJavaObject()) {
            Object o = ((BasicJavaObjectValue) rv).getValue();
            if (o instanceof Boolean) {
                return (Boolean) o;
            }
            //TODO extend the conversion in case of other simple types
            return o != null;
        }
        throw new BasicRuntimeException("Can not convert value to boolean");
    }
}

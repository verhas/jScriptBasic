package com.scriptbasic.executors;

import com.scriptbasic.interfaces.BasicRuntimeException;
import com.scriptbasic.interfaces.RightValue;

public class BasicStringValue extends AbstractPrimitiveRightValue<String> {
    public BasicStringValue(final String s) {
        setValue(s);
    }

    private BasicStringValue() {
    }

    private static BasicStringValue singleton = new BasicStringValue();

    public static String convert(RightValue rv) throws BasicRuntimeException {
        return singleton.convertRightValue(rv);
    }

    @Override
    @SuppressWarnings("unchecked")
    protected String convertRightValue(RightValue rv) throws BasicRuntimeException {
        if (rv.isString() || rv.isNumeric() || rv.isBoolean()
                || rv.isJavaObject()) {
            return ((AbstractPrimitiveRightValue<Object>) rv).getValue()
                    .toString();
        }

        throw new BasicRuntimeException("Can not convert value to string");
    }
}

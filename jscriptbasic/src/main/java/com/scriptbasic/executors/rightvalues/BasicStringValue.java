package com.scriptbasic.executors.rightvalues;

import com.scriptbasic.exceptions.BasicRuntimeException;
import com.scriptbasic.interfaces.RightValue;

public class BasicStringValue extends AbstractPrimitiveRightValue<String> {
    public BasicStringValue(final String s) {
        setValue(s);
    }

    @SuppressWarnings("unchecked")
    public static String convert(final RightValue rv)
            throws BasicRuntimeException {
        if (rv.isString() || rv.isNumeric() || rv.isBoolean()
                || rv.isJavaObject()) {
            return ((AbstractPrimitiveRightValue<Object>) rv).getValue()
                    .toString();
        }
        throw new BasicRuntimeException("Can not convert value to string");
    }
}

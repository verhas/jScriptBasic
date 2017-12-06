package com.scriptbasic.executors.rightvalues;

import com.scriptbasic.api.BasicRuntimeException;
import com.scriptbasic.interfaces.RightValue;

public class BasicStringValue extends AbstractPrimitiveRightValue<String> {
    public BasicStringValue(final String s) {
        setValue(s);
    }

    @SuppressWarnings("unchecked")
    public static String asString(final RightValue rv)
            throws BasicRuntimeException {
        try {
            String resultString = null;
            if (rv == null
                    || ((AbstractPrimitiveRightValue<Object>) rv).getValue() == null) {
                resultString = "undef";
            } else if (rv.isString() || rv.isNumeric() || rv.isBoolean()
                    || rv.isJavaObject()) {
                resultString = ((AbstractPrimitiveRightValue<Object>) rv)
                        .getValue().toString();
            } else {
                throw new BasicRuntimeException("Can not convert value to string");
            }
            return resultString;
        } catch (final ClassCastException cce) {
            throw new BasicRuntimeException("Error converting to string", cce);
        }
    }
}

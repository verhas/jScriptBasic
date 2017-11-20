package com.scriptbasic.executors.rightvalues;

import com.scriptbasic.interfaces.BasicRuntimeException;
import com.scriptbasic.interfaces.RightValue;

public class BasicJavaObjectValue extends AbstractPrimitiveRightValue<Object> {
    
    public BasicJavaObjectValue(Object value) {
        setValue(value);
    }

    public static Object asObject(final RightValue arv)
            throws BasicRuntimeException {
        if (arv.isJavaObject()) {
            return ((BasicJavaObjectValue) arv).getValue();
        }
        throw new BasicRuntimeException("Can not convert value to object");
    }

}

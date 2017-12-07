package com.scriptbasic.executors.rightvalues;

import com.scriptbasic.interfaces.BasicRuntimeException;
import com.scriptbasic.spi.RightValue;

public class BasicJavaObjectValue extends AbstractPrimitiveRightValue<Object> {

    public BasicJavaObjectValue(final Object value) {
        setValue(value);
    }

    public static Object asObject(final RightValue arv)
            throws BasicRuntimeException {
        if (arv.isJavaObject()) {
            return ((BasicJavaObjectValue) arv).getValue();
        }
        throw new BasicRuntimeException("Can not convert value to object");
    }
    @Override
    public String toString(){
        try {
            return asObject(this).toString();
        } catch (BasicRuntimeException e) {
            return super.toString();
        }
    }
}

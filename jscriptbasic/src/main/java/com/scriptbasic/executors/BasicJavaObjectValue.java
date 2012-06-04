package com.scriptbasic.executors;

import com.scriptbasic.interfaces.BasicRuntimeException;
import com.scriptbasic.interfaces.RightValue;

public class BasicJavaObjectValue extends AbstractPrimitiveRightValue<Object> {

    private BasicJavaObjectValue() {
    }

    private static BasicJavaObjectValue singleton = new BasicJavaObjectValue();

    public static Object convert(RightValue rv) throws BasicRuntimeException {
        return singleton.convertRightValue(rv);
    }

    @Override
    protected Object convertRightValue(RightValue arv)
            throws BasicRuntimeException {
        if (arv.isJavaObject()) {
            return ((BasicJavaObjectValue) arv).getValue();
        }
        throw new BasicRuntimeException("Can not convert value to object");
    }

}

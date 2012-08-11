package com.scriptbasic.executors.rightvalues;

import com.scriptbasic.interfaces.BasicRuntimeException;
import com.scriptbasic.interfaces.RightValue;

public final class BasicBooleanValue extends
        AbstractPrimitiveRightValue<Boolean> {
    public BasicBooleanValue(final Boolean b) {
        setValue(b);
    }

    private static Boolean convertNumeric(
            AbstractNumericRightValue<Number> originalValue) {
        Boolean convertedValue = null;
        if (originalValue.isLong()) {
            final Long l = (Long) originalValue.getValue();
            convertedValue = l != null && l != 0;
        } else if (originalValue.isDouble()) {
            final Double d = (Double) originalValue.getValue();
            convertedValue = d != null && d != 0;
        }
        return convertedValue;
    }

    @SuppressWarnings("unchecked")
    public static Boolean convert(final RightValue rv)
            throws BasicRuntimeException {
        Boolean convertedValue = null;
        if (rv == null) {
            convertedValue = Boolean.FALSE;
        } else if (rv instanceof AbstractNumericRightValue<?>) {
            convertedValue = convertNumeric((AbstractNumericRightValue<Number>) rv);
        } else if (rv.isBoolean()) {
            convertedValue = ((BasicBooleanValue) rv).getValue();
        } else if (rv.isString()) {
            final String s = ((BasicStringValue) rv).getValue();
            convertedValue = s != null && s.length() > 0;
        } else if (rv.isJavaObject()) {
            final Object o = ((BasicJavaObjectValue) rv).getValue();
            if (o instanceof Boolean) {
                convertedValue = (Boolean) o;
            } else {
                // TODO elaborate the conversion with other object classes, like
                convertedValue = o != null;
            }
        }
        return convertedValue;
        // throw new BasicRuntimeException("Can not convert value to boolean");
    }
}

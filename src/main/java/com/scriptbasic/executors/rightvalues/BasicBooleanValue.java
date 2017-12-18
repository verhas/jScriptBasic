package com.scriptbasic.executors.rightvalues;

import com.scriptbasic.interfaces.BasicRuntimeException;
import com.scriptbasic.spi.BasicValue;
import com.scriptbasic.spi.RightValue;

public final class BasicBooleanValue extends AbstractPrimitiveRightValue<Boolean> {

    public BasicBooleanValue(final Boolean b) {
        setValue(b);
    }

    private static Boolean convertNumeric(
            final AbstractNumericRightValue<Number> originalValue) {
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

    public static Boolean asBoolean(final RightValue originalValue)
            throws BasicRuntimeException {
        Boolean convertedValue = null;

        if (originalValue == null) {
            convertedValue = Boolean.FALSE;
        } else if (originalValue instanceof AbstractNumericRightValue) {
            convertedValue = convertNumeric((AbstractNumericRightValue) originalValue);
        } else if (originalValue.isBoolean()) {
            convertedValue = ((BasicBooleanValue) originalValue).getValue();
        } else if (originalValue.isString()) {
            final String s = ((BasicStringValue) originalValue).getValue();
            convertedValue = s != null && s.length() > 0;
        } else if (originalValue.isJavaObject()) {
            final Object o = ((BasicJavaObjectValue) originalValue).getValue();
            if (o instanceof Boolean) {
                convertedValue = (Boolean) o;
            } else {
                // TODO elaborate the conversion with other object classes, like
                convertedValue = o != null;
            }
        }
        return convertedValue;
    }
    @Override
    public String toString(){
        try {
            return asBoolean(this).toString();
        } catch (BasicRuntimeException e) {
            return super.toString();
        }
    }
}

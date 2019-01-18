package com.scriptbasic.executors.rightvalues;

import com.scriptbasic.spi.RightValue;

public final class BasicBooleanValue extends AbstractPrimitiveRightValue<Boolean> {

    public BasicBooleanValue(final Boolean b) {
        setValue(b);
    }

    private static Boolean convertNumeric(
            final AbstractNumericRightValue<Number> originalValue) {
        final Boolean convertedValue;
        if (originalValue.isLong()) {
            final var l = (Long) originalValue.getValue();
            convertedValue = l != null && l != 0;
        } else if (originalValue.isDouble()) {
            final var d = (Double) originalValue.getValue();
            convertedValue = d != null && d != 0;
        } else {
            convertedValue = null;
        }
        return convertedValue;
    }

    public static Boolean asBoolean(final RightValue originalValue) {
        final Boolean convertedValue;

        if (originalValue == null) {
            convertedValue = Boolean.FALSE;
        } else if (originalValue instanceof AbstractNumericRightValue) {
            convertedValue = convertNumeric((AbstractNumericRightValue) originalValue);
        } else if (originalValue.isBoolean()) {
            convertedValue = ((BasicBooleanValue) originalValue).getValue();
        } else if (originalValue.isString()) {
            final var s = ((BasicStringValue) originalValue).getValue();
            convertedValue = s != null && s.length() > 0;
        } else if (originalValue.isJavaObject()) {
            final var o = ((BasicJavaObjectValue) originalValue).getValue();
            if (o instanceof Boolean) {
                convertedValue = (Boolean) o;
            } else {
                // TODO elaborate the conversion with other object classes
                convertedValue = o != null;
            }
        } else {
            convertedValue = null;
        }
        return convertedValue;
    }

    @Override
    public String toString() {
        return asBoolean(this).toString();
    }
}

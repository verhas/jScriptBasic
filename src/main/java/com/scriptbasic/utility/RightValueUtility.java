package com.scriptbasic.utility;

import com.scriptbasic.api.ScriptBasicException;
import com.scriptbasic.errors.BasicInterpreterInternalError;
import com.scriptbasic.executors.rightvalues.*;
import com.scriptbasic.interfaces.BasicRuntimeException;
import com.scriptbasic.spi.BasicArray;
import com.scriptbasic.spi.RightValue;

/**
 * @author Peter Verhas date June 26, 2012
 */
public final class RightValueUtility {
    private RightValueUtility() {
        NoInstance.isPossible();
    }

    @SuppressWarnings("unchecked")
    public static Object getValueObject(final RightValue arg) {
        final Object object;
        if (arg == null) {
            object = null;
        } else if (arg instanceof AbstractPrimitiveRightValue<?>) {
            object = ((AbstractPrimitiveRightValue<Object>) arg).getValue();
        } else if (arg instanceof BasicArray) {
            object = arg;
        } else {
            throw new BasicInterpreterInternalError(
                    "The class of the object "
                            + arg
                            + " is not convertible type to fetch it's value as object.");
        }
        return object;
    }

    @SuppressWarnings("unchecked")
    public static Integer convert2Integer(final RightValue index)
            throws ScriptBasicException {
        if (index.isNumeric()) {
            return ((AbstractNumericRightValue<Number, Object>) index).getNumericValue().intValue();
        } else {
            throw new BasicRuntimeException(
                    index.toString()
                            + " is not a numeric value, can not be used to index and array");
        }
    }

    /**
     * Create a right value from the object. If the object is already a right
     * value then just return the object. If the object is some primitive object
     * (Long, Integer, Short, Float, Double, String, Character, Boolean then it
     * creates a Basic object that holds a Long, Double, String or Boolean
     * value.
     * <p>
     * In other cases the method will return a {@link BasicJavaObjectValue}.
     *
     * @param value the original value to convert to a right value
     * @return the converted right value
     */
    public static RightValue createRightValue(final Object value) {
        final RightValue rightValue;
        if (value instanceof RightValue) {
            rightValue = (RightValue) value;
        } else if (value instanceof Double) {
            rightValue = new BasicDoubleValue((Double) value);
        } else if (value instanceof Float) {
            rightValue = new BasicDoubleValue(((Float) value).doubleValue());
        } else if (value instanceof Long) {
            rightValue = new BasicLongValue((Long) value);
        } else if (value instanceof Integer) {
            rightValue = new BasicLongValue(((Integer) value).longValue());
        } else if (value instanceof Short) {
            rightValue = new BasicLongValue(((Short) value).longValue());
        } else if (value instanceof Byte) {
            rightValue = new BasicLongValue(((Byte) value).longValue());
        } else if (value instanceof String) {
            rightValue = new BasicStringValue((String) value);
        } else if (value instanceof Character) {
            rightValue = new BasicStringValue(new String(
                    new char[]{(Character) value}));
        } else if (value instanceof Boolean) {
            rightValue = new BasicBooleanValue((Boolean) value);
        } else if (value == EmptyValue.EMPTY_VALUE) {
            rightValue = BasicEmptyValue.EMPTY_VALUE;
        } else {
            rightValue = new BasicJavaObjectValue(value);
        }
        return rightValue;
    }

    /**
     * Create a right value array from an object array. Allocate the new array
     * and convert each element of the object array into a right value.
     *
     * @param values the object array to convert to right values
     * @return the array of right values converted from the object array
     */
    public static RightValue[] createRightValues(final Object[] values) {
        if (values != null) {
            final RightValue[] rightValues = new RightValue[values.length];
            for (int i = 0; i < values.length; i++) {
                rightValues[i] = createRightValue(values[i]);
            }
            return rightValues;
        } else {
            return null;
        }
    }
}

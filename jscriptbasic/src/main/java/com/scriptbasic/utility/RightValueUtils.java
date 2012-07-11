/**
 * 
 */
package com.scriptbasic.utility;

import com.scriptbasic.errors.BasicInterpreterInternalError;
import com.scriptbasic.executors.rightvalues.AbstractNumericRightValue;
import com.scriptbasic.executors.rightvalues.AbstractPrimitiveRightValue;
import com.scriptbasic.executors.rightvalues.BasicBooleanValue;
import com.scriptbasic.executors.rightvalues.BasicDoubleValue;
import com.scriptbasic.executors.rightvalues.BasicJavaObjectValue;
import com.scriptbasic.executors.rightvalues.BasicLongValue;
import com.scriptbasic.executors.rightvalues.BasicStringValue;
import com.scriptbasic.interfaces.BasicRuntimeException;
import com.scriptbasic.interfaces.ExecutionException;
import com.scriptbasic.interfaces.RightValue;

/**
 * @author Peter Verhas
 * @date June 26, 2012
 * 
 */
public final class RightValueUtils {
    private RightValueUtils() {
        UtilityUtility.throwExceptionToEnsureNobodyCallsIt();
    }

    @SuppressWarnings("unchecked")
    public static Object getValueObject(RightValue arg) {
        Object object;
        if (arg instanceof AbstractPrimitiveRightValue<?>) {
            object = ((AbstractPrimitiveRightValue<Object>) arg).getValue();
        } else {
            throw new BasicInterpreterInternalError(
                    "The class of the object "
                            + arg
                            + " is not convertible type to fetchs it's value as object.");
        }
        return object;
    }

    /**
     * @param index
     * @return
     */
    @SuppressWarnings("unchecked")
    public static Integer convert2Integer(RightValue index)
            throws ExecutionException {
        Integer result = 0;
        if (index.isNumeric()) {
            result = ((AbstractNumericRightValue<Number>) index).getValue()
                    .intValue();
        } else {
            throw new BasicRuntimeException(
                    index.toString()
                            + " is not a numeric value, can not be used to index and array");
        }
        return result;
    }

    public static RightValue createRightValue(Object value) {
        RightValue rightValue = null;
        if (value instanceof Double) {
            rightValue = new BasicDoubleValue((Double) value);
        } else if (value instanceof Float) {
            rightValue = new BasicDoubleValue(((Float) value).doubleValue());
        } else if (value instanceof Long) {
            rightValue = new BasicLongValue((Long) value);
        } else if (value instanceof Integer) {
            rightValue = new BasicLongValue(((Integer) value).longValue());
        } else if (value instanceof String) {
            rightValue = new BasicStringValue((String) value);
        } else if (value instanceof Boolean) {
            rightValue = new BasicBooleanValue((Boolean) value);
        } else {
            rightValue = new BasicJavaObjectValue(value);
        }
        return rightValue;
    }
}

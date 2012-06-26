/**
 * 
 */
package com.scriptbasic.utility;

import com.scriptbasic.executors.rightvalues.BasicBooleanValue;
import com.scriptbasic.executors.rightvalues.BasicDoubleValue;
import com.scriptbasic.executors.rightvalues.BasicJavaObjectValue;
import com.scriptbasic.executors.rightvalues.BasicLongValue;
import com.scriptbasic.executors.rightvalues.BasicStringValue;
import com.scriptbasic.interfaces.RightValue;

/**
 * @author Peter Verhas
 * @date Jun 26, 2012
 * 
 */
public class RightValueUtils {
    private RightValueUtils() {
    }

    public static RightValue createRightValue(Object value) {
        RightValue rightValue = null;
        if (value instanceof Boolean) {
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

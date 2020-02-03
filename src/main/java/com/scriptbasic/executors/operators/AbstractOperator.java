package com.scriptbasic.executors.operators;

import com.scriptbasic.executors.AbstractExpression;
import com.scriptbasic.executors.rightvalues.BasicBooleanValue;
import com.scriptbasic.executors.rightvalues.BasicDoubleValue;
import com.scriptbasic.executors.rightvalues.BasicJavaObjectValue;
import com.scriptbasic.executors.rightvalues.BasicLongValue;
import com.scriptbasic.executors.rightvalues.BasicStringValue;
import com.scriptbasic.interfaces.BasicRuntimeException;
import com.scriptbasic.spi.RightValue;

public abstract class AbstractOperator extends AbstractExpression {

    /**
     * Try to convert operator to double
     * 
     * @param op
     *            operator
     * @return Return double or null if cannot be converted
     */
    static protected Double getAsDouble(RightValue op) {
        try {
            return BasicDoubleValue.asDouble(op);
        } catch (BasicRuntimeException e) {
            return null;
        }
    }

    /**
     * Try to convert operator to long
     * 
     * @param op
     *            operator
     * @return Return long or null if cannot be converted
     */
    static protected Long getAsLong(RightValue op) {
        try {
            return BasicLongValue.asLong(op);
        } catch (BasicRuntimeException e) {
            return null;
        }
    }

    /**
     * Try to convert operator to boolean
     * 
     * @param op
     *            operator
     * @return Return boolean or null if cannot be converted
     */
    static protected Boolean getAsBoolean(RightValue op) {
        try {
            return BasicBooleanValue.asBoolean(op);
        } catch (BasicRuntimeException e) {
            return null;
        }
    }

    /**
     * Try to convert operator to string
     * 
     * @param op
     *            operator
     * @return Return string or null if cannot be converted
     */
    static protected String getAsString(RightValue op) {
        try {
            return BasicStringValue.asString(op);
        } catch (BasicRuntimeException e) {
            return null;
        }
    }

    /**
     * Try to convert operator to java object
     * 
     * @param op
     *            operator
     * @return Return object or null if cannot be converted
     */
    static protected Object getAsObject(RightValue op) {
        try {
            return BasicJavaObjectValue.asObject(op);
        } catch (BasicRuntimeException e) {
            return null;
        }
    }
}

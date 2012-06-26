package com.scriptbasic.executors.rightvalues;

import com.scriptbasic.interfaces.BasicRuntimeException;
import com.scriptbasic.interfaces.Expression;
import com.scriptbasic.interfaces.ExtendedInterpreter;
import com.scriptbasic.interfaces.RightValue;

public abstract class AbstractPrimitiveRightValue<T> extends AbstractRightValue
        implements Expression {
    protected T value;

    public T getValue() {
        return this.value;
    }

    public void setValue(final T value) {
        this.value = value;
    }

    @Override
    public RightValue evaluate(ExtendedInterpreter extendedInterpreter) throws BasicRuntimeException {
        return this;
    }

    public boolean getBooleanValue() throws BasicRuntimeException {
        if (value == null) {
            return false;
        }
        if (value instanceof Boolean) {
            return (Boolean) value;
        }
        if (value instanceof Long) {
            return ((long) (Long) value) != 0;
        }
        if (value instanceof Double) {
            return ((double) (Double) value) != 0.0;
        }
        if (value instanceof String) {
            return ((String) value).length() > 0;
        }
        throw new BasicRuntimeException("Value '" + value + "' of class '"
                + value.getClass() + "' can not be cast to boolean");
    }

}

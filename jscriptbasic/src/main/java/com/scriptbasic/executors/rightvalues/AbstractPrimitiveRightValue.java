package com.scriptbasic.executors.rightvalues;

import com.scriptbasic.exceptions.BasicRuntimeException;
import com.scriptbasic.interfaces.Expression;
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
    public RightValue evaluate() throws BasicRuntimeException {
        return this;
    }

}

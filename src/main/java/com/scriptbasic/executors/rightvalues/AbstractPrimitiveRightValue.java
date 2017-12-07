package com.scriptbasic.executors.rightvalues;

import com.scriptbasic.api.BasicValue;
import com.scriptbasic.interfaces.BasicRuntimeException;
import com.scriptbasic.interfaces.Expression;
import com.scriptbasic.interfaces.Interpreter;
import com.scriptbasic.api.RightValue;

public abstract class AbstractPrimitiveRightValue<T> implements Expression, RightValue, BasicValue {
    private T value;

    public T getValue() {
        return this.value;
    }

    public void setValue(final T value) {
        this.value = value;
    }

    @Override
    public RightValue evaluate(final Interpreter interpreter) throws BasicRuntimeException {
        return this;
    }

}

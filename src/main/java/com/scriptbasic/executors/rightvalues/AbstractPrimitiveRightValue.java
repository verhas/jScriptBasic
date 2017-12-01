package com.scriptbasic.executors.rightvalues;

import com.scriptbasic.interfaces.BasicRuntimeException;
import com.scriptbasic.interfaces.Expression;
import com.scriptbasic.interfaces.ExtendedInterpreter;
import com.scriptbasic.interfaces.RightValue;

public abstract class AbstractPrimitiveRightValue<T> extends AbstractRightValue
        implements Expression {
    private T value;

    public T getValue() {
        return this.value;
    }

    public void setValue(final T value) {
        this.value = value;
    }

    @Override
    public RightValue evaluate(final ExtendedInterpreter extendedInterpreter) throws BasicRuntimeException {
        return this;
    }

}

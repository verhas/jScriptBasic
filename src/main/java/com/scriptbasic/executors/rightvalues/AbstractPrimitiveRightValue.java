package com.scriptbasic.executors.rightvalues;

import com.scriptbasic.context.CompilerContext;
import com.scriptbasic.interfaces.Expression;
import com.scriptbasic.spi.BasicValue;
import com.scriptbasic.spi.Interpreter;
import com.scriptbasic.spi.RightValue;

public abstract class AbstractPrimitiveRightValue<T> implements Expression, RightValue, BasicValue<T> {
    private T value;

    @Override
    public String toJava(CompilerContext cc) {
        return "" + getValue();
    }

    public T getValue() {
        return this.value;
    }

    public void setValue(final T value) {
        this.value = value;
    }

    @Override
    public RightValue evaluate(final Interpreter interpreter) {
        return this;
    }

}

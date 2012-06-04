package com.scriptbasic.executors;

import com.scriptbasic.interfaces.RightValue;

public abstract class AbstractRightValue extends AbstractValue implements
        RightValue {

    @Override
    public final Boolean isNumeric() {
        return isLong() || isDouble();
    }

    @Override
    public final Boolean isLong() {
        return this instanceof BasicLongValue;
    }

    @Override
    public final Boolean isDouble() {
        return this instanceof BasicDoubleValue;
    }

    @Override
    public final Boolean isBoolean() {
        return this instanceof BasicBooleanValue;
    }

    @Override
    public final Boolean isString() {
        return this instanceof BasicStringValue;
    }

    @Override
    public final Boolean isArray() {
        return this instanceof BasicArrayValue;
    }

    @Override
    public final Boolean isJavaObject() {
        return this instanceof BasicJavaObjectValue;
    }
}

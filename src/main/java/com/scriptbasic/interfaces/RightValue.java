package com.scriptbasic.interfaces;

public interface RightValue extends Value {
    Boolean isNumeric();

    Boolean isLong();

    Boolean isDouble();

    Boolean isString();

    Boolean isBoolean();

    Boolean isArray();

    Boolean isJavaObject();
}

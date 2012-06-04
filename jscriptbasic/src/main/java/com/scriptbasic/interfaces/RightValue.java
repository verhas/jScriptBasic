package com.scriptbasic.interfaces;

public interface RightValue {
    public Boolean isNumeric();

    public Boolean isLong();

    public Boolean isDouble();

    public Boolean isString();

    public Boolean isBoolean();

    // TODO is this OK?
    public Boolean isArray();

    public Boolean isJavaObject();
}

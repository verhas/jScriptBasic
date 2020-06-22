package com.scriptbasic.executors.rightvalues;

public class EmptyValue {

    public static EmptyValue EMPTY_VALUE = new EmptyValue();

    private EmptyValue() {
    }

    @Override
    public String toString() {
        return "";
    }
}

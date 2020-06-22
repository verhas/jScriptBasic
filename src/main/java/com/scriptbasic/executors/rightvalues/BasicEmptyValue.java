package com.scriptbasic.executors.rightvalues;

public class BasicEmptyValue extends AbstractNumericRightValue<Long, EmptyValue> {

    public static final BasicEmptyValue EMPTY_VALUE = new BasicEmptyValue();

    private BasicEmptyValue() {
        setValue(EmptyValue.EMPTY_VALUE);
    }

    @Override
    public Long getNumericValue() {
        return 0L;
    }

}

package com.scriptbasic.executors.rightvalues;

public abstract class AbstractNumericRightValue<N extends Number, T> extends
        AbstractPrimitiveRightValue<T> {

    /**
     * Return numeric value representing this right value
     * 
     * @return numeric value
     */
    public abstract N getNumericValue();

}

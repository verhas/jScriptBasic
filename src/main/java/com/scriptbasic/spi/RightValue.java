package com.scriptbasic.spi;

import com.scriptbasic.executors.rightvalues.*;
import com.scriptbasic.interfaces.Value;

public interface RightValue extends Value {
    default Boolean isNumeric() {
        return isLong() || isDouble() || isDate();
    }

    default Boolean isLong() {
        return this instanceof BasicLongValue;
    }

    default Boolean isDouble() {
        return this instanceof BasicDoubleValue;
    }

    default Boolean isBoolean() {
        return this instanceof BasicBooleanValue;
    }

    default Boolean isString() {
        return this instanceof BasicStringValue;
    }

    default Boolean isArray() {
        return this instanceof BasicArrayValue;
    }

    default Boolean isDate() {
        return this instanceof BasicDateValue;
    }

    default Boolean isJavaObject() {
        return this instanceof BasicJavaObjectValue;
    }

}

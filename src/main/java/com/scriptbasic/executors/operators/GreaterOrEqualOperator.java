package com.scriptbasic.executors.operators;

import com.scriptbasic.executors.rightvalues.*;
import com.scriptbasic.interfaces.BasicRuntimeException;
import com.scriptbasic.spi.RightValue;

public class GreaterOrEqualOperator extends AbstractCompareOperator {

    @Override
    protected Boolean compareTo(final BasicDoubleValue f, final RightValue op)
            throws BasicRuntimeException {
        return f.getValue() >= BasicDoubleValue.asDouble(op,"Cannot float compare >= on undef value");
    }

    @Override
    protected Boolean compareTo(final BasicLongValue f, final RightValue op)
            throws BasicRuntimeException {
        return f.getValue() >= BasicLongValue.asLong(op,"Cannot integer compare >= undef value");
    }

    @Override
    protected Boolean compareTo(final BasicStringValue f, final RightValue op)
            throws BasicRuntimeException {
        return f.getValue().compareTo(BasicStringValue.asString(op)) >= 0;
    }

    @Override
    protected Boolean compareTo(final BasicJavaObjectValue f,
                                final RightValue op) throws BasicRuntimeException {
        return compareJavaObjectTo(f, op) >= 0;
    }

    @Override
    protected Boolean compareTo(final BasicBooleanValue f, final RightValue op) {
        final var a = f.getValue() ? 1 : 0;
        final var b = BasicBooleanValue.asBoolean(op) ? 1 : 0;
        return a >= b;
    }

}

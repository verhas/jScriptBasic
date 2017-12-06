package com.scriptbasic.executors.operators;

import com.scriptbasic.executors.rightvalues.*;
import com.scriptbasic.api.BasicRuntimeException;
import com.scriptbasic.interfaces.RightValue;

public class GreaterThanOperator extends AbstractCompareOperator {

    @Override
    protected Boolean compareTo(final BasicDoubleValue f, final RightValue op)
            throws BasicRuntimeException {
        return f.getValue() > BasicDoubleValue.asDouble(op);
    }

    @Override
    protected Boolean compareTo(final BasicLongValue f, final RightValue op)
            throws BasicRuntimeException {
        return f.getValue() > BasicLongValue.asLong(op);
    }

    @Override
    protected Boolean compareTo(final BasicStringValue f, final RightValue op)
            throws BasicRuntimeException {
        return f.getValue().compareTo(BasicStringValue.asString(op)) > 0;
    }

    @Override
    protected Boolean compareTo(final BasicJavaObjectValue f,
                                final RightValue op) throws BasicRuntimeException {
        return compareJavaObjectTo(f, op) > 0;
    }

    @Override
    protected Boolean compareTo(final BasicBooleanValue f, final RightValue op)
            throws BasicRuntimeException {
        final int a = f.getValue() ? 1 : 0;
        final int b = BasicBooleanValue.asBoolean(op) ? 1 : 0;
        return a > b;
    }

}

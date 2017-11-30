package com.scriptbasic.executors.operators;

import com.scriptbasic.executors.rightvalues.*;
import com.scriptbasic.interfaces.BasicRuntimeException;
import com.scriptbasic.interfaces.RightValue;

public class EqualsOperator extends AbstractCompareOperator {

    @Override
    protected Boolean compareTo(final BasicDoubleValue f, final RightValue op)
            throws BasicRuntimeException {
        return f.getValue().equals(BasicDoubleValue.asDouble(op));
    }

    @Override
    protected Boolean compareTo(final BasicLongValue f, final RightValue op)
            throws BasicRuntimeException {
        return f.getValue().equals(BasicLongValue.asLong(op));
    }

    @Override
    protected Boolean compareTo(final BasicStringValue f, final RightValue op)
            throws BasicRuntimeException {
        return f.getValue().equals(BasicStringValue.asString(op));
    }

    @Override
    protected Boolean compareTo(final BasicJavaObjectValue f,
            final RightValue op) throws BasicRuntimeException {
        return compareJavaObjectTo(f, op) == 0;
    }

    @Override
    protected Boolean compareTo(final BasicBooleanValue f, final RightValue op)
            throws BasicRuntimeException {
        return f.getValue().equals(BasicBooleanValue.asBoolean(op));
    }

}

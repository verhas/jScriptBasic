package com.scriptbasic.executors.operators;
import com.scriptbasic.executors.rightvalues.BasicBooleanValue;
import com.scriptbasic.executors.rightvalues.BasicDoubleValue;
import com.scriptbasic.executors.rightvalues.BasicJavaObjectValue;
import com.scriptbasic.executors.rightvalues.BasicLongValue;
import com.scriptbasic.executors.rightvalues.BasicStringValue;
import com.scriptbasic.interfaces.BasicRuntimeException;
import com.scriptbasic.interfaces.RightValue;
public class EqualsOperator extends AbstractCompareOperator {
    @Override
    protected Boolean compareTo(final BasicDoubleValue f, final RightValue op)
            throws BasicRuntimeException {
        return f.getValue().equals(BasicDoubleValue.convert(op));
    }
    @Override
    protected Boolean compareTo(final BasicLongValue f, final RightValue op)
            throws BasicRuntimeException {
        return f.getValue().equals(BasicLongValue.convert(op));
    }
    @Override
    protected Boolean compareTo(final BasicStringValue f, final RightValue op)
            throws BasicRuntimeException {
        return f.getValue().equals(BasicStringValue.convert(op));
    }
    @Override
    protected Boolean compareTo(final BasicJavaObjectValue f,
            final RightValue op) throws BasicRuntimeException {
        return compareJavaObjectTo(f, op) == 0;
    }
    @Override
    protected Boolean compareTo(final BasicBooleanValue f, final RightValue op)
            throws BasicRuntimeException {
        return f.getValue().equals(BasicBooleanValue.convert(op));
    }
}
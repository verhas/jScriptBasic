package com.scriptbasic.executors.operators;

import com.scriptbasic.executors.BasicBooleanValue;
import com.scriptbasic.interfaces.BasicRuntimeException;
import com.scriptbasic.interfaces.Expression;
import com.scriptbasic.interfaces.RightValue;

public class LogicalOrOperator extends AbstractShortCircuitBinaryOperator {

    @Override
    protected RightValue evaluateOn(final RightValue leftOperand,
            final Expression rightOperand) throws BasicRuntimeException {
        final BasicBooleanValue bbv = new BasicBooleanValue(true);
        if (BasicBooleanValue.convert(leftOperand)) {
            return bbv;
        } else {
            return new BasicBooleanValue(BasicBooleanValue.convert(rightOperand.evaluate()));
        }
    }
}

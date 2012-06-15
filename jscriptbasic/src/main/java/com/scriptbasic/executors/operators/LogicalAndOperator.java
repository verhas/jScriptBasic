package com.scriptbasic.executors.operators;

import com.scriptbasic.exceptions.BasicRuntimeException;
import com.scriptbasic.executors.rightvalues.BasicBooleanValue;
import com.scriptbasic.interfaces.Expression;
import com.scriptbasic.interfaces.RightValue;

public class LogicalAndOperator extends AbstractShortCircuitBinaryOperator {

    @Override
    protected RightValue evaluateOn(final RightValue leftOperand,
            final Expression rightOperand) throws BasicRuntimeException {
        final BasicBooleanValue bbv = new BasicBooleanValue(false);
        if (BasicBooleanValue.convert(leftOperand)) {
            return new BasicBooleanValue(BasicBooleanValue.convert(rightOperand
                    .evaluate()));
        } else {
            return bbv;
        }
    }
}

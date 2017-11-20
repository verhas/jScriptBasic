package com.scriptbasic.executors.operators;

import com.scriptbasic.executors.rightvalues.BasicBooleanValue;
import com.scriptbasic.interfaces.ExecutionException;
import com.scriptbasic.interfaces.Expression;
import com.scriptbasic.interfaces.ExtendedInterpreter;
import com.scriptbasic.interfaces.RightValue;

public class LogicalAndOperator extends AbstractShortCircuitBinaryOperator {

    @Override
    protected RightValue evaluateOn(ExtendedInterpreter extendedInterpreter,
            RightValue leftOperand, final Expression rightOperand)
            throws ExecutionException {
        final BasicBooleanValue bbv = new BasicBooleanValue(false);
        if (BasicBooleanValue.asBoolean(leftOperand)) {
            return new BasicBooleanValue(BasicBooleanValue.asBoolean(rightOperand
                    .evaluate(extendedInterpreter)));
        } else {
            return bbv;
        }
    }
}

package com.scriptbasic.executors.operators;

import com.scriptbasic.executors.rightvalues.BasicBooleanValue;
import com.scriptbasic.api.ScriptBasicException;
import com.scriptbasic.interfaces.Expression;
import com.scriptbasic.interfaces.Interpreter;
import com.scriptbasic.interfaces.RightValue;

public class LogicalAndOperator extends AbstractShortCircuitBinaryOperator {

    @Override
    protected RightValue evaluateOn(final Interpreter interpreter,
                                    final RightValue leftOperand, final Expression rightOperand)
            throws ScriptBasicException {
        final BasicBooleanValue bbv = new BasicBooleanValue(false);
        if (BasicBooleanValue.asBoolean(leftOperand)) {
            return new BasicBooleanValue(BasicBooleanValue.asBoolean(rightOperand
                    .evaluate(interpreter)));
        } else {
            return bbv;
        }
    }
}

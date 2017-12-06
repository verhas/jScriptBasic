package com.scriptbasic.executors.operators;

import com.scriptbasic.executors.rightvalues.BasicBooleanValue;
import com.scriptbasic.interfaces.ExecutionException;
import com.scriptbasic.interfaces.Interpreter;
import com.scriptbasic.interfaces.RightValue;

public class UnaryOperatorNot extends AbstractUnaryOperator {

    @Override
    public RightValue evaluate(final Interpreter extendedInterpreter)
            throws ExecutionException {
        new BasicBooleanValue(false);
        return new BasicBooleanValue(!BasicBooleanValue.asBoolean(getOperand()
                .evaluate(extendedInterpreter)));
    }
}

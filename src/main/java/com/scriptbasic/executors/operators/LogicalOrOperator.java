package com.scriptbasic.executors.operators;

import com.scriptbasic.api.ScriptBasicException;
import com.scriptbasic.context.CompilerContext;
import com.scriptbasic.executors.rightvalues.BasicBooleanValue;
import com.scriptbasic.interfaces.Expression;
import com.scriptbasic.spi.Interpreter;
import com.scriptbasic.spi.RightValue;

public class LogicalOrOperator extends AbstractShortCircuitBinaryOperator {
    @Override
    protected String operatorToJava(final CompilerContext cc) {
        return "||";
    }

    @Override
    protected RightValue evaluateOn(final Interpreter interpreter,
                                    final RightValue leftOperand, final Expression rightOperand)
            throws ScriptBasicException {
        final var bbv = new BasicBooleanValue(true);
        if (BasicBooleanValue.asBoolean(leftOperand)) {
            return bbv;
        } else {
            return new BasicBooleanValue(BasicBooleanValue.asBoolean(rightOperand
                    .evaluate(interpreter)));
        }
    }
}

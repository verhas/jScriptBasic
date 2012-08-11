package com.scriptbasic.executors.operators;
import com.scriptbasic.executors.rightvalues.BasicBooleanValue;
import com.scriptbasic.interfaces.ExecutionException;
import com.scriptbasic.interfaces.Expression;
import com.scriptbasic.interfaces.ExtendedInterpreter;
import com.scriptbasic.interfaces.RightValue;
public class LogicalOrOperator extends AbstractShortCircuitBinaryOperator {
    @Override
    protected RightValue evaluateOn(ExtendedInterpreter extendedInterpreter,
            final RightValue leftOperand, final Expression rightOperand)
            throws ExecutionException {
        final BasicBooleanValue bbv = new BasicBooleanValue(true);
        if (BasicBooleanValue.convert(leftOperand)) {
            return bbv;
        } else {
            return new BasicBooleanValue(BasicBooleanValue.convert(rightOperand
                    .evaluate(extendedInterpreter)));
        }
    }
}
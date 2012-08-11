package com.scriptbasic.executors.operators;
import com.scriptbasic.interfaces.ExecutionException;
import com.scriptbasic.interfaces.Expression;
import com.scriptbasic.interfaces.ExtendedInterpreter;
import com.scriptbasic.interfaces.RightValue;
public abstract class AbstractShortCircuitBinaryOperator extends
        AbstractBinaryOperator {
    protected abstract RightValue evaluateOn(
            ExtendedInterpreter extendedInterpreter, RightValue leftOperand,
            Expression rightOperand) throws ExecutionException;
    @Override
    public RightValue evaluate(ExtendedInterpreter extendedInterpreter)
            throws ExecutionException {
        return evaluateOn(extendedInterpreter,
                getLeftOperand().evaluate(extendedInterpreter),
                getRightOperand());
    }
}
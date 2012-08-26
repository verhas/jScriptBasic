package com.scriptbasic.executors.operators;

import com.scriptbasic.interfaces.BasicRuntimeException;
import com.scriptbasic.interfaces.ExecutionException;
import com.scriptbasic.interfaces.ExtendedInterpreter;
import com.scriptbasic.interfaces.RightValue;

/**
 * This class is extended by the operator classes that implement an operation
 * that evaluates both operand. These are usually the numeric operands, as
 * opposed to the logical 'and' and 'or' operators that evaluate the second
 * operand only if the first operand is true and/or false.
 * 
 * @author Peter Verhas
 * date May 31, 2012
 */
public abstract class AbstractBinaryFullCircuitOperator extends
        AbstractBinaryOperator {

    protected abstract RightValue evaluateOn(RightValue leftOperand,
            RightValue rightOperand) throws BasicRuntimeException;

    @Override
    public final RightValue evaluate(ExtendedInterpreter extendedInterpreter) throws ExecutionException {
        return evaluateOn(getLeftOperand().evaluate(extendedInterpreter), getRightOperand()
                .evaluate(extendedInterpreter));
    }

}

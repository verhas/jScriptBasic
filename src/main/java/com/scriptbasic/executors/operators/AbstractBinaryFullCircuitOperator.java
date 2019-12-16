package com.scriptbasic.executors.operators;

import com.scriptbasic.api.ScriptBasicException;
import com.scriptbasic.spi.Interpreter;
import com.scriptbasic.spi.RightValue;

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
	
	protected RightValue getLeftEvaluatedOperand(final Interpreter interpreter) throws ScriptBasicException {
		return getLeftOperand().evaluate(interpreter);
	}

	protected RightValue getRightEvaluatedOperand(final Interpreter interpreter) throws ScriptBasicException {
		return getRightOperand().evaluate(interpreter);
	}

	protected abstract RightValue evaluateOn(RightValue leftOperand,
                                             RightValue rightOperand) throws ScriptBasicException;

    @Override
    public final RightValue evaluate(final Interpreter interpreter) throws ScriptBasicException {
        return evaluateOn(getLeftEvaluatedOperand(interpreter), 
        		getRightEvaluatedOperand(interpreter));
    }

}

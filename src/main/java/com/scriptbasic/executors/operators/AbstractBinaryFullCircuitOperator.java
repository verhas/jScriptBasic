package com.scriptbasic.executors.operators;

import com.scriptbasic.api.ScriptBasicException;
import com.scriptbasic.context.CompilerContext;
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

    /**
     * Function used to evaluate left operand.
     * Override this function if custom evaluation is needed.
     *
     * @param interpreter current interpreter
     * @return evaluated operand
     * @throws ScriptBasicException when the evaluation of the operand cannot be performed w/o error
     */
    protected RightValue getLeftOperandEvaluated(final Interpreter interpreter) throws ScriptBasicException {
        return getLeftOperand().evaluate(interpreter);
    }

    /**
     * Function used to evaluate right operand.
     * Override this function if custom evaluation is needed.
     *
     * @param interpreter current interpreter
     * @return evaluated operand
     * @throws ScriptBasicException when the evaluation of the operand cannot be performed w/o error
     */
    protected RightValue getRightOperandEvaluated(final Interpreter interpreter) throws ScriptBasicException {
        return getRightOperand().evaluate(interpreter);
    }

    protected abstract RightValue evaluateOn(RightValue leftOperand,
                                             RightValue rightOperand) throws ScriptBasicException;

    protected abstract String operatorToJava(CompilerContext cc);

    @Override
    public String toJava(CompilerContext cc) {
        return "(" + getLeftOperand().toJava(cc) + ")" + operatorToJava(cc) + "(" + getRightOperand().toJava(cc) + ")";
    }

    @Override
    public final RightValue evaluate(final Interpreter interpreter) throws ScriptBasicException {
        return evaluateOn(getLeftOperandEvaluated(interpreter),
                getRightOperandEvaluated(interpreter));
    }

}

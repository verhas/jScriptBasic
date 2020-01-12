package com.scriptbasic.executors.operators;

import com.scriptbasic.api.ScriptBasicException;
import com.scriptbasic.spi.Interpreter;
import com.scriptbasic.spi.RightValue;

public class RightSideGreaterOrEqualOperator extends GreaterOrEqualOperator {
    
    /**
     * Evaluated left operand
     */    
	private final RightValue leftOperandEvaluated;

	public RightSideGreaterOrEqualOperator(final RightValue leftOperandEvaluated) {
		this.leftOperandEvaluated = leftOperandEvaluated;
	}

	@Override
	protected RightValue getLeftOperandEvaluated(final Interpreter interpreter) {
		return this.leftOperandEvaluated;
	}
}

package com.scriptbasic.executors.operators;

import com.scriptbasic.api.ScriptBasicException;
import com.scriptbasic.spi.Interpreter;
import com.scriptbasic.spi.RightValue;

public class RightSideLessOrEqualOperator extends LessOrEqualOperator {
	final private RightValue leftEvaluatedOperand;
	
	public RightSideLessOrEqualOperator(final RightValue leftEvaluatedOperand) {
		this.leftEvaluatedOperand = leftEvaluatedOperand;
	}

	@Override
	protected RightValue getLeftEvaluatedOperand(final Interpreter interpreter) throws ScriptBasicException {
		return this.leftEvaluatedOperand;
	}

}

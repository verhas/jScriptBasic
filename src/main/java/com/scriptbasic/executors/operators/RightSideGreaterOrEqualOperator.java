package com.scriptbasic.executors.operators;

import com.scriptbasic.api.ScriptBasicException;
import com.scriptbasic.spi.Interpreter;
import com.scriptbasic.spi.RightValue;

public class RightSideGreaterOrEqualOperator extends GreaterOrEqualOperator {
	private RightValue leftEvaluatedOperand;

	public RightSideGreaterOrEqualOperator(final RightValue leftEvaluatedOperand)
	{
		this.leftEvaluatedOperand = leftEvaluatedOperand;
	}

	@Override
	protected RightValue getLeftEvaluatedOperand(final Interpreter interpreter) throws ScriptBasicException {
		return this.leftEvaluatedOperand;
	}
}

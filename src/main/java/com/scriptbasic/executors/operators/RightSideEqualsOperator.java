package com.scriptbasic.executors.operators;

import com.scriptbasic.api.ScriptBasicException;
import com.scriptbasic.spi.Interpreter;
import com.scriptbasic.spi.RightValue;

public class RightSideEqualsOperator
   extends EqualsOperator
{
	final private RightValue leftEvaluatedOperand;
	
	public RightSideEqualsOperator(final RightValue leftEvaluatedOperand) {
		this.leftEvaluatedOperand = leftEvaluatedOperand;
	}

	@Override
	protected RightValue getLeftEvaluatedOperand(final Interpreter interpreter) throws ScriptBasicException {
		return this.leftEvaluatedOperand;
	}

}

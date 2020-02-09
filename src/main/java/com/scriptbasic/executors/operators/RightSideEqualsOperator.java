package com.scriptbasic.executors.operators;

import com.scriptbasic.spi.Interpreter;
import com.scriptbasic.spi.RightValue;

public class RightSideEqualsOperator
   extends EqualsOperator {
    
    /**
     * Evaluated left operand
     */
	final private RightValue leftOperandEvaluated;
	
	public RightSideEqualsOperator(final RightValue leftOperandEvaluated) {
		this.leftOperandEvaluated = leftOperandEvaluated;
	}

	@Override
	protected RightValue getLeftOperandEvaluated(final Interpreter interpreter) {
		return this.leftOperandEvaluated;
	}

}

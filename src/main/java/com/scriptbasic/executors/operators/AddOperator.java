package com.scriptbasic.executors.operators;

import com.scriptbasic.executors.rightvalues.BasicBooleanValue;
import com.scriptbasic.executors.rightvalues.BasicDoubleValue;
import com.scriptbasic.executors.rightvalues.BasicJavaObjectValue;
import com.scriptbasic.executors.rightvalues.BasicLongValue;
import com.scriptbasic.executors.rightvalues.BasicStringValue;
import com.scriptbasic.interfaces.BasicRuntimeException;
import com.scriptbasic.interfaces.RightValue;
import com.scriptbasic.main.TestCommandLine.MySecurityManager;

public class AddOperator extends AbstractBinaryFullCircuitHalfDoubleOperator {

	@Override
	protected RightValue operateOnDoubleDouble(final Double a, final Double b)
			throws BasicRuntimeException {
		return new BasicDoubleValue(a + b);
	}

	@Override
	protected RightValue operateOnLongLong(final Long a, final Long b)
			throws BasicRuntimeException {
		return new BasicLongValue(a + b);
	}

	private static String myToString(Object o) {
		if (o == null)
			return "null";
		return o.toString();
	}

	private static String getString(final RightValue op)
			throws BasicRuntimeException {
		if (op.isString()) {
			return ((BasicStringValue) op).getValue();
		}
		if (op.isDouble()) {
			return myToString(((BasicDoubleValue) op).getValue());
		}
		if (op.isLong()) {
			return myToString(((BasicLongValue) op).getValue());
		}
		if (op.isBoolean()) {
			return myToString(((BasicBooleanValue) op).getValue());
		}
		if (op.isJavaObject()) {
			return myToString(((BasicJavaObjectValue) op).getValue());
		}
		throw new BasicRuntimeException(
				"Argument can not be converted to string");
	}

	@Override
	protected RightValue operateOnValues(final RightValue leftOperand,
			final RightValue rightOperand) throws BasicRuntimeException {
		return new BasicStringValue(getString(leftOperand)
				+ getString(rightOperand));
	}

	@Override
	protected String operatorName() {
		return "Plus";
	}

}

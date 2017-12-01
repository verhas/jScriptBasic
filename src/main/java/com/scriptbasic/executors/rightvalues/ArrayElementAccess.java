package com.scriptbasic.executors.rightvalues;

import com.scriptbasic.executors.AbstractIdentifieredExpressionListedExpression;
import com.scriptbasic.interfaces.*;
import com.scriptbasic.utility.RightValueUtility;

public class ArrayElementAccess extends
        AbstractIdentifieredExpressionListedExpression {

    @Override
    public RightValue evaluate(final ExtendedInterpreter interpreter)
            throws ExecutionException {
        final VariableMap variableMap = interpreter.getVariables();
        RightValue value = variableMap.getVariableValue(getVariableName());
        value = interpreter.getHook().variableRead(getVariableName(), value);
        for (final Expression expression : getExpressionList()) {
            if (value instanceof BasicArrayValue) {
                final BasicArrayValue arrayVar = (BasicArrayValue) value;
                final Integer index = RightValueUtility.convert2Integer(expression
                        .evaluate(interpreter));
                final Object object = arrayVar.get(index);
                if (object instanceof RightValue) {
                    value = (RightValue) object;
                } else {
                    value = new BasicJavaObjectValue(object);
                    arrayVar.set(index, value);
                }
            }
        }
        return value;
    }

}

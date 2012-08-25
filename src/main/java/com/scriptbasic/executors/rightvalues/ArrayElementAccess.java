package com.scriptbasic.executors.rightvalues;

import com.scriptbasic.executors.AbstractIdentifieredExpressionListedExpression;
import com.scriptbasic.interfaces.ExecutionException;
import com.scriptbasic.interfaces.Expression;
import com.scriptbasic.interfaces.ExtendedInterpreter;
import com.scriptbasic.interfaces.RightValue;
import com.scriptbasic.interfaces.VariableMap;
import com.scriptbasic.utility.RightValueUtility;

public class ArrayElementAccess extends
        AbstractIdentifieredExpressionListedExpression {

    @Override
    public RightValue evaluate(ExtendedInterpreter interpreter)
            throws ExecutionException {
        VariableMap variableMap = interpreter.getVariables();
        RightValue value = variableMap.getVariableValue(getVariableName());
        value = interpreter.getHook().variableRead(getVariableName(), value);
        for (Expression expression : getExpressionList()) {
            if (value instanceof BasicArrayValue) {
                BasicArrayValue arrayVar = (BasicArrayValue) value;
                Integer index = RightValueUtility.convert2Integer(expression
                        .evaluate(interpreter));
                Object object = arrayVar.get(index);
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

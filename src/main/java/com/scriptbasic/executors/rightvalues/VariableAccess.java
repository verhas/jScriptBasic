package com.scriptbasic.executors.rightvalues;

import com.scriptbasic.executors.AbstractIdentifieredExpression;
import com.scriptbasic.interfaces.ExecutionException;
import com.scriptbasic.interfaces.Interpreter;
import com.scriptbasic.interfaces.RightValue;
import com.scriptbasic.interfaces.VariableMap;

public class VariableAccess extends AbstractIdentifieredExpression {

    @Override
    public RightValue evaluate(final Interpreter interpreter)
            throws ExecutionException {
        final VariableMap variableMap = interpreter.getVariables();
        RightValue value = variableMap.getVariableValue(getVariableName());
        value = interpreter.getHook().variableRead(getVariableName(), value);
        return value;
    }
}

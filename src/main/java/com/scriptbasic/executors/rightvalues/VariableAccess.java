package com.scriptbasic.executors.rightvalues;

import com.scriptbasic.executors.AbstractIdentifieredExpression;
import com.scriptbasic.api.ScriptBasicException;
import com.scriptbasic.spi.Interpreter;
import com.scriptbasic.spi.RightValue;
import com.scriptbasic.interfaces.VariableMap;

public class VariableAccess extends AbstractIdentifieredExpression {

    @Override
    public RightValue evaluate(final Interpreter interpreter)
            throws ScriptBasicException {
        final VariableMap variableMap = interpreter.getVariables();
        RightValue value = variableMap.getVariableValue(getVariableName());
        value = interpreter.getHook().variableRead(getVariableName(), value);
        return value;
    }
}

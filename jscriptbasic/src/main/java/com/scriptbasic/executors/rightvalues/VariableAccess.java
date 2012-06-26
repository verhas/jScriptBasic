package com.scriptbasic.executors.rightvalues;

import com.scriptbasic.executors.AbstractIdentifieredExpression;
import com.scriptbasic.interfaces.ExecutionException;
import com.scriptbasic.interfaces.ExtendedInterpreter;
import com.scriptbasic.interfaces.RightValue;
import com.scriptbasic.interfaces.VariableMap;

public class VariableAccess extends AbstractIdentifieredExpression {

    @Override
    public RightValue evaluate(ExtendedInterpreter extendedInterpreter) throws ExecutionException {
        VariableMap variableMap = extendedInterpreter.getVariables();
        return variableMap.getVariableValue(getVariableName());
    }
}

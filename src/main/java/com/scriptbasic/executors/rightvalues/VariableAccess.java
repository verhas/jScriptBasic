package com.scriptbasic.executors.rightvalues;

import com.scriptbasic.api.ScriptBasicException;
import com.scriptbasic.context.CompilerContext;
import com.scriptbasic.executors.AbstractIdentifieredExpression;
import com.scriptbasic.interfaces.VariableMap;
import com.scriptbasic.spi.Interpreter;
import com.scriptbasic.spi.RightValue;

public class VariableAccess extends AbstractIdentifieredExpression {

    @Override
    public String toJava(CompilerContext cc){
        final var code = fragment();
        final var variable = cc.serial(getVariableName());
        code.add("variable(%d)",variable);
        return code.close();
    }

    @Override
    public RightValue evaluate(final Interpreter interpreter)
            throws ScriptBasicException {
        final VariableMap variableMap = interpreter.getVariables();
        RightValue value = variableMap.getVariableValue(getVariableName());
        if (value == null) {
            value = BasicEmptyValue.EMPTY_VALUE;
        }
        value = interpreter.getHook().variableRead(getVariableName(), value);
        return value;
    }
}

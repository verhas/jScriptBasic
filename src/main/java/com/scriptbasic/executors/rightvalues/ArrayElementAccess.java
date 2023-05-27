package com.scriptbasic.executors.rightvalues;

import com.scriptbasic.api.ScriptBasicException;
import com.scriptbasic.context.CompilerContext;
import com.scriptbasic.executors.AbstractIdentifieredExpressionListedExpression;
import com.scriptbasic.interfaces.Expression;
import com.scriptbasic.interfaces.VariableMap;
import com.scriptbasic.spi.BasicArray;
import com.scriptbasic.spi.Interpreter;
import com.scriptbasic.spi.RightValue;
import com.scriptbasic.utility.RightValueUtility;

public class ArrayElementAccess extends
        AbstractIdentifieredExpressionListedExpression {

    @Override
    public RightValue evaluate(final Interpreter interpreter)
            throws ScriptBasicException {
        final VariableMap variableMap = interpreter.getVariables();
        RightValue value = variableMap.getVariableValue(getVariableName());
        value = interpreter.getHook().variableRead(getVariableName(), value);
        for (final Expression expression : getExpressionList()) {
            if (value instanceof final BasicArray arrayVar) {
                final var index = RightValueUtility.convert2Integer(expression
                        .evaluate(interpreter));
                final var object = arrayVar.get(index);
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

    @Override
    public String toJava(CompilerContext cc) {
        final var sb = new StringBuilder();
        final var name = getVariableName();

        sb.append("((BasicArrayValue)interpreter.getVariables().getVariableValue(\"%s\"))".formatted(name));

        for (final Expression expression : getExpressionList()) {
            sb.append(".get(");
            sb.append(expression.toJava(cc));
            sb.append(")");
        }
        return sb.toString();
    }
}

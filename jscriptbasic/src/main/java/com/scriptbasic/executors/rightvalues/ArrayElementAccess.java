package com.scriptbasic.executors.rightvalues;

import com.scriptbasic.executors.AbstractIdentifieredExpressionListedExpression;
import com.scriptbasic.interfaces.ExecutionException;
import com.scriptbasic.interfaces.Expression;
import com.scriptbasic.interfaces.ExtendedInterpreter;
import com.scriptbasic.interfaces.RightValue;
import com.scriptbasic.interfaces.VariableMap;
import com.scriptbasic.utility.RightValueUtils;

public class ArrayElementAccess extends
        AbstractIdentifieredExpressionListedExpression {

    @Override
    public RightValue evaluate(ExtendedInterpreter extendedInterpreter)
            throws ExecutionException {
        VariableMap variableMap = extendedInterpreter.getVariables();
        RightValue variable = variableMap.getVariableValue(getVariableName());
        for (Expression expression : getExpressionList()) {
            if (variable instanceof BasicArrayValue) {
                BasicArrayValue arrayVar = (BasicArrayValue) variable;
                Integer index = RightValueUtils.convert2Integer(expression
                        .evaluate(extendedInterpreter));
                Object object = arrayVar.get(index);
                if (object instanceof RightValue) {
                    variable = (RightValue) object;
                }else{
                    variable = new BasicJavaObjectValue(object);
                    arrayVar.set(index, variable);
                }
            }
        }
        return variable;
    }

}

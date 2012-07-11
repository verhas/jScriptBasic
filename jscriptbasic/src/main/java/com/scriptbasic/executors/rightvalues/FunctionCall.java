package com.scriptbasic.executors.rightvalues;

import com.scriptbasic.executors.AbstractIdentifieredExpressionListedExpression;
import com.scriptbasic.interfaces.BasicRuntimeException;
import com.scriptbasic.interfaces.ExtendedInterpreter;
import com.scriptbasic.interfaces.RightValue;

public class FunctionCall extends
        AbstractIdentifieredExpressionListedExpression {

    @Override
    public RightValue evaluate(ExtendedInterpreter extendedInterpreter)
            throws BasicRuntimeException {
        // TODO implement F(x) evaluation. This has to recognize BASIC
        // functions, or declared Java static methods or object methods using
        // registered objects
        return null;
    }

}

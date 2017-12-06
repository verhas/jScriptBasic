package com.scriptbasic.executors.rightvalues;

import com.scriptbasic.executors.AbstractIdentifieredExpressionListedExpression;
import com.scriptbasic.executors.commands.CommandSub;
import com.scriptbasic.interfaces.*;
import com.scriptbasic.utility.ExpressionUtility;
import com.scriptbasic.utility.ReflectionUtility;
import com.scriptbasic.utility.RightValueUtility;

import java.lang.reflect.Method;
import java.util.Iterator;
import java.util.List;

public class FunctionCall extends
        AbstractIdentifieredExpressionListedExpression {

    private boolean commandNeedLookup = true;
    private CommandSub commandSub = null;

    private static RightValue[] evaluateArguments(final ExpressionList argumentList,
                                                  final Interpreter interpreter) throws ExecutionException {
        final RightValue[] argumentValues;
        if (argumentList == null) {
            argumentValues = null;
        } else {
            final Iterator<Expression> expressionIterator = argumentList.iterator();
            argumentValues = new RightValue[argumentList.size()];
            for (int i = 0; i < argumentValues.length; i++) {
                argumentValues[i] = expressionIterator.next().evaluate(
                        interpreter);
            }
        }
        return argumentValues;
    }

    private RightValue callBasicFunction(final Interpreter interpreter)
            throws ExecutionException {
        return ExpressionUtility.callBasicFunction(interpreter,
                evaluateArguments(getExpressionList(), interpreter),
                commandSub, getVariableName());
    }

    private RightValue callJavaFunction(final Interpreter interpreter)
            throws ExecutionException {
        RightValue result = null;
        final String functionName = getVariableName();
        final List<RightValue> args = ExpressionUtility.evaluateExpressionList(
                interpreter, getExpressionList());
        final Method method = interpreter.getJavaMethod(null, functionName);
        if (method == null) {
            throw new BasicRuntimeException("There is no callable function '"
                    + functionName + "'");
        } else {
            Object methodResult = null;
            methodResult = ReflectionUtility.invoke(functionName, interpreter,
                    method, null, args);
            result = RightValueUtility.createRightValue(methodResult);
        }
        return result;
    }

    private void lookUpCommandSub(final Interpreter interpreter) {
        if (commandNeedLookup) {
            commandNeedLookup = false;
            commandSub = interpreter.getSubroutine(getVariableName());
        }
    }

    @Override
    public RightValue evaluate(final Interpreter interpreter)
            throws ExecutionException {

        lookUpCommandSub(interpreter);
        RightValue result = null;
        if (commandSub == null) {
            result = callJavaFunction(interpreter);
        } else {
            result = callBasicFunction(interpreter);
        }
        return result;
    }
}

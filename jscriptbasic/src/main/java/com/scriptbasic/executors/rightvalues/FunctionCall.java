package com.scriptbasic.executors.rightvalues;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Iterator;
import java.util.List;

import com.scriptbasic.executors.AbstractIdentifieredExpressionListedExpression;
import com.scriptbasic.executors.commands.CommandSub;
import com.scriptbasic.executors.leftvalues.BasicLeftValue;
import com.scriptbasic.interfaces.BasicRuntimeException;
import com.scriptbasic.interfaces.ExecutionException;
import com.scriptbasic.interfaces.Expression;
import com.scriptbasic.interfaces.ExpressionList;
import com.scriptbasic.interfaces.ExtendedInterpreter;
import com.scriptbasic.interfaces.LeftValue;
import com.scriptbasic.interfaces.LeftValueList;
import com.scriptbasic.interfaces.RightValue;
import com.scriptbasic.utility.ExpressionUtility;
import com.scriptbasic.utility.RightValueUtility;

public class FunctionCall extends
        AbstractIdentifieredExpressionListedExpression {

    private RightValue callBasicFunction(ExtendedInterpreter extendedInterpreter)
            throws ExecutionException {
        RightValue result = null;
        RightValue[] argumentValues = evaluateArguments(getExpressionList(),
                extendedInterpreter);
        extendedInterpreter.push();
        LeftValueList arguments = commandSub.getArguments();
        registerLocalVariablesWithValues(arguments, argumentValues,
                extendedInterpreter);
        extendedInterpreter.setReturnValue(null);
        extendedInterpreter.execute(commandSub.getNextCommand());
        result = extendedInterpreter.getReturnValue();
        extendedInterpreter.pop();
        return result;
    }

    private RightValue callJavaFunction(ExtendedInterpreter extendedInterpreter)
            throws ExecutionException {
        RightValue result = null;
        String functionName = getVariableName();
        List<RightValue> args = ExpressionUtility.evaluateExpressionList(
                extendedInterpreter, getExpressionList());
        Method method = null;
        method = extendedInterpreter.getJavaMethod(null, functionName);
        if (method != null) {
            Object methodResultObject = null;
            try {
                methodResultObject = method.invoke(null, ExpressionUtility
                        .getObjectArray(args, method, extendedInterpreter));
            } catch (IllegalAccessException | IllegalArgumentException
                    | InvocationTargetException e) {
                throw new BasicRuntimeException("Can not invoke method "
                        + functionName, e);
            } catch (Exception e) {
                throw new BasicRuntimeException("Invoking function '"
                        + functionName + "' throws exception:", e);
            }
            result = RightValueUtility.createRightValue(methodResultObject);
        }
        return result;
    }

    private boolean commandNeedLookup = true;
    private CommandSub commandSub = null;

    private void looupCommandSub(ExtendedInterpreter extendedInterpreter) {
        if (commandNeedLookup) {
            commandNeedLookup = false;
            commandSub = extendedInterpreter.getSubroutine(getVariableName());
        }
    }

    @Override
    public RightValue evaluate(ExtendedInterpreter extendedInterpreter)
            throws ExecutionException {

        looupCommandSub(extendedInterpreter);
        RightValue result = null;
        if (commandSub == null) {
            result = callJavaFunction(extendedInterpreter);
        } else {
            result = callBasicFunction(extendedInterpreter);
        }
        return result;
    }

    private static RightValue[] evaluateArguments(ExpressionList argumentList,
            ExtendedInterpreter extendedInterpreter) throws ExecutionException {
        RightValue[] argumentValues;
        if (argumentList == null) {
            argumentValues = null;
        } else {
            Iterator<Expression> expressionIterator = argumentList.iterator();
            argumentValues = new RightValue[argumentList.size()];
            for (int i = 0; i < argumentValues.length; i++) {
                argumentValues[i] = expressionIterator.next().evaluate(
                        extendedInterpreter);
            }
        }
        return argumentValues;
    }

    /**
     * @param arguments
     * @param argumentValues
     * @throws ExecutionException
     */
    private static void registerLocalVariablesWithValues(
            LeftValueList arguments, RightValue[] argumentValues,
            ExtendedInterpreter extendedInterpreter) throws ExecutionException {
        if (arguments != null) {
            Iterator<LeftValue> argumentIterator = arguments.iterator();
            for (int i = 0; i < argumentValues.length; i++) {
                LeftValue argument = argumentIterator.next();
                if (argument instanceof BasicLeftValue) {
                    String name = ((BasicLeftValue) argument).getIdentifier();
                    extendedInterpreter.getVariables().registerLocalVariable(
                            name);
                    extendedInterpreter.setVariable(name, argumentValues[i]);
                } else {
                    throw new BasicRuntimeException(
                            "subroutine formal argument is erroneous");
                }
            }
        }
    }
}

package com.scriptbasic.executors.rightvalues;

import java.util.Iterator;

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

public class FunctionCall extends
        AbstractIdentifieredExpressionListedExpression {

    @Override
    public RightValue evaluate(ExtendedInterpreter extendedInterpreter)
            throws ExecutionException {
        RightValue result = null;
        String functionName = getVariableName();
        ExpressionList argumentList = getExpressionList();
        CommandSub commandSub = extendedInterpreter.getSubroutine(functionName);
        if (commandSub != null) {
            RightValue[] argumentValues = evaluateArguments(argumentList,
                    extendedInterpreter);
            extendedInterpreter.push();
            LeftValueList arguments = commandSub.getArguments();
            registerLocalVariablesWithValues(arguments, argumentValues,
                    extendedInterpreter);
            extendedInterpreter.setReturnValue(null);
            extendedInterpreter.execute(commandSub.getNextCommand());
            result = extendedInterpreter.getReturnValue();
            extendedInterpreter.pop();
        } else {
            // TODO implement F(x) evaluation when F is not a subroutine.
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

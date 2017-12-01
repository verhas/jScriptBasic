package com.scriptbasic.utility;

import com.scriptbasic.executors.commands.CommandSub;
import com.scriptbasic.executors.leftvalues.BasicLeftValue;
import com.scriptbasic.executors.operators.JavaObjectFieldAccessOperator;
import com.scriptbasic.executors.rightvalues.BasicStringValue;
import com.scriptbasic.executors.rightvalues.VariableAccess;
import com.scriptbasic.interfaces.*;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * @author Peter Verhas date June 28, 2012
 */
public final class ExpressionUtility {
    private ExpressionUtility() {
        NoInstance.isPossible();
    }

    private static boolean parameterLengthMatch(Class<?>[] parameterTypes,
                                                List<RightValue> args) {
        if (args == null) {
            return true;
        }
        return parameterTypes.length >= args.size();
    }

    public static RightValue callBasicFunction(ExtendedInterpreter interpreter,
                                               RightValue[] argumentValues, CommandSub commandSub,
                                               String functionName) throws ExecutionException {
        RightValue result = null;
        interpreter.push();
        LeftValueList arguments = commandSub.getArguments();
        registerLocalVariablesWithValues(arguments, argumentValues, interpreter);
        interpreter.disableHook();
        interpreter.setReturnValue(null);
        interpreter.enableHook();
        interpreter.getHook().beforeSubroutineCall(functionName, arguments,
                argumentValues);
        interpreter.execute(commandSub.getNextCommand());
        result = interpreter.getReturnValue();
        interpreter.pop();
        return result;
    }

    /**
     * @param arguments
     * @param argumentValues
     * @throws ExecutionException
     */
    private static void registerLocalVariablesWithValues(
            LeftValueList arguments, RightValue[] argumentValues,
            ExtendedInterpreter interpreter) throws ExecutionException {
        if (arguments != null) {
            Iterator<LeftValue> argumentIterator = arguments.iterator();
            for (RightValue argumentValue : argumentValues) {
                if (argumentIterator.hasNext()) {
                    LeftValue argument = argumentIterator.next();
                    if (argument instanceof BasicLeftValue) {
                        String name = ((BasicLeftValue) argument)
                                .getIdentifier();
                        interpreter.getVariables().registerLocalVariable(name);
                        interpreter.setVariable(name, argumentValue);
                    } else {
                        throw new BasicRuntimeException(
                                "subroutine formal argument list is too long");
                    }
                } else {
                    throw new BasicRuntimeException(
                            "subroutine formal argument is erroneous");
                }
            }
        }
    }

    public static Object[] getObjectArray(List<RightValue> args, Method method,
                                          ExtendedInterpreter extendedInterpreter) throws ExecutionException {
        Class<?>[] parameterTypes = method.getParameterTypes();
        // if the declaring class of the method implements the interface
        // WHATEVER //TODO find a good name for the interface that is to be
        // implemented by the embedding Java program
        // and the first parameter of the method is
        // com.scriptbasic.interfaces.interpreter then auto magically
        // pass that parameter to the method
        if (!parameterLengthMatch(parameterTypes, args)) {
            throw new BasicRuntimeException(
                    "Different number of parameters calling the Java method '"
                            + method.getName() + "'");
        }
        final ArrayList<Object> result = new ArrayList<>();
        int parameterIndex = 0;
        if (args != null) {
            for (RightValue arg : args) {
                Object object = CastUtility.cast(
                        RightValueUtility.getValueObject(arg),
                        parameterTypes[parameterIndex]);
                result.add(object);
                parameterIndex++;
            }
        }
        // if there are missing arguments, use undef
        while (parameterIndex < parameterTypes.length) {
            result.add(null);
            parameterIndex++;
        }
        return result.isEmpty() ? null : result.toArray(new Object[0]);
    }

    public static List<RightValue> evaluateExpressionList(
            ExtendedInterpreter extendedInterpreter,
            ExpressionList expressionList) throws ExecutionException {
        List<RightValue> args = null;
        if (expressionList != null) {
            args = new LinkedList<>();
            for (Expression expression : expressionList) {
                args.add(expression.evaluate(extendedInterpreter));
            }
        }
        return args;
    }

    /**
     * Convert an expression of the form 'a.b.c.d' into a String.
     *
     * @param expression
     * @return the string containing the dots and the identifiers
     * @throws AnalysisException when the expression does not match the format
     */
    public static String convertToString(Expression expression)
            throws AnalysisException {
        if (expression instanceof VariableAccess) {
            return ((VariableAccess) expression).getVariableName();
        }
        if (expression instanceof JavaObjectFieldAccessOperator) {
            JavaObjectFieldAccessOperator ofao = (JavaObjectFieldAccessOperator) expression;
            return convertToString(ofao.getLeftOperand()) + "."
                    + convertToString(ofao.getRightOperand());
        }
        if (expression instanceof BasicStringValue) {
            return ((BasicStringValue) expression).getValue();
        }
        throw new BasicSyntaxException(
                "class, package or symbol name are not vaid in command USE");
    }
}

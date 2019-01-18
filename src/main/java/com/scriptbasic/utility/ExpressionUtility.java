package com.scriptbasic.utility;

import com.scriptbasic.api.ScriptBasicException;
import com.scriptbasic.executors.commands.CommandSub;
import com.scriptbasic.executors.leftvalues.BasicLeftValue;
import com.scriptbasic.executors.operators.JavaObjectFieldAccessOperator;
import com.scriptbasic.executors.rightvalues.BasicStringValue;
import com.scriptbasic.executors.rightvalues.VariableAccess;
import com.scriptbasic.interfaces.*;
import com.scriptbasic.spi.Interpreter;
import com.scriptbasic.spi.LeftValue;
import com.scriptbasic.spi.LeftValueList;
import com.scriptbasic.spi.RightValue;

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

    private static boolean parameterLengthMatch(final Class<?>[] parameterTypes,
                                                final List<RightValue> args,
                                                final boolean interpreterAware) {
        if (args == null) {
            return true;
        }
        return interpreterAware ? parameterTypes.length >= args.size() + 1 : parameterTypes.length >= args.size();
    }

    public static RightValue callBasicFunction(final Interpreter interpreter,
                                               final RightValue[] argumentValues, final CommandSub commandSub,
                                               final String functionName) throws ScriptBasicException {
        final RightValue result;
        interpreter.push();
        final var arguments = commandSub.getArguments();
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
     * @param arguments      parameter
     * @param argumentValues parameter
     * @throws ScriptBasicException in case of exception
     */
    private static void registerLocalVariablesWithValues(
            final LeftValueList arguments, final RightValue[] argumentValues,
            final Interpreter interpreter) throws ScriptBasicException {
        if (arguments != null) {
            final Iterator<LeftValue> argumentIterator = arguments.iterator();
            for (final RightValue argumentValue : argumentValues) {
                if (argumentIterator.hasNext()) {
                    final var argument = argumentIterator.next();
                    if (argument instanceof BasicLeftValue) {
                        final var name = ((BasicLeftValue) argument)
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

    public static Object[] getObjectArray(final List<RightValue> args, final Method method,
                                          final Interpreter interpreter) throws ScriptBasicException {
        final Class<?>[] parameterTypes = method.getParameterTypes();
        // if the first parameter of the method is
        // com.scriptbasic.interfaces.interpreter then auto magically
        // pass that parameter to the method
        final var interpreterAware = parameterTypes.length >= 1 && Interpreter.class.isAssignableFrom(parameterTypes[0]);
        if (!parameterLengthMatch(parameterTypes, args, interpreterAware)) {
            throw new BasicRuntimeException(
                    "Different number of parameters calling the Java method '"
                            + method.getName() + "'");
        }
        final ArrayList<Object> result = new ArrayList<>();
        int parameterIndex;
        if (interpreterAware) {
            result.add(interpreter);
            parameterIndex = 1;
        } else {
            parameterIndex = 0;
        }
        if (args != null) {
            for (final RightValue arg : args) {
                final var object = CastUtility.cast(
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
            final Interpreter interpreter,
            final ExpressionList expressionList) throws ScriptBasicException {
        List<RightValue> args = null;
        if (expressionList != null) {
            args = new LinkedList<>();
            for (final Expression expression : expressionList) {
                args.add(expression.evaluate(interpreter));
            }
        }
        return args;
    }

    /**
     * Convert an expression of the form 'a.b.c.d' into a String.
     *
     * @param expression parameter
     * @return the string containing the dots and the identifiers
     * @throws AnalysisException when the expression does not match the format
     */
    public static String convertToString(final Expression expression)
            throws AnalysisException {
        if (expression instanceof VariableAccess) {
            return ((VariableAccess) expression).getVariableName();
        }
        if (expression instanceof JavaObjectFieldAccessOperator) {
            final var ofao = (JavaObjectFieldAccessOperator) expression;
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

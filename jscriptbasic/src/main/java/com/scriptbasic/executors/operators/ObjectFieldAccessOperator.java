package com.scriptbasic.executors.operators;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import com.scriptbasic.errors.BasicInterpreterInternalError;
import com.scriptbasic.executors.rightvalues.AbstractPrimitiveRightValue;
import com.scriptbasic.executors.rightvalues.ArrayElementAccess;
import com.scriptbasic.executors.rightvalues.FunctionCall;
import com.scriptbasic.executors.rightvalues.VariableAccess;
import com.scriptbasic.interfaces.BasicRuntimeException;
import com.scriptbasic.interfaces.ExecutionException;
import com.scriptbasic.interfaces.Expression;
import com.scriptbasic.interfaces.ExpressionList;
import com.scriptbasic.interfaces.ExtendedInterpreter;
import com.scriptbasic.interfaces.RightValue;
import com.scriptbasic.utility.CastUtility;
import com.scriptbasic.utility.RightValueUtils;

/**
 * This is the highest priority operator (priority 1) that is used to access a
 * field of an object. This operator is the (.) dot operator.
 * 
 * @author Peter Verhas
 * 
 */
public class ObjectFieldAccessOperator extends AbstractBinaryOperator {

    private RightValue fetchField(ExtendedInterpreter extendedInterpreter)
            throws ExecutionException {
        RightValue result = null;
        Object object = getLeftOperandObject(extendedInterpreter);
        VariableAccess rightOp = (VariableAccess) getRightOperand();
        String fieldName = rightOp.getVariableName();
        Class<?> klass = object.getClass();
        try {
            Field field = klass.getField(fieldName);
            result = RightValueUtils.createRightValue(field.get(object));
        } catch (NoSuchFieldException | SecurityException
                | IllegalArgumentException | IllegalAccessException e) {
            throw new BasicRuntimeException("Object access of type "
                    + object.getClass() + " can not access field '" + fieldName
                    + "'", e);
        }
        return result;
    }

    private static List<RightValue> evaluateExpressionList(
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

    @SuppressWarnings("unchecked")
    private static Class<?>[] getClassArray(List<RightValue> args) {
        ArrayList<Class<?>> result = null;
        if (args != null) {
            result = new ArrayList<>();
            for (RightValue arg : args) {
                Class<?> klass = null;
                if (arg instanceof AbstractPrimitiveRightValue<?>) {
                    klass = ((AbstractPrimitiveRightValue<Object>) arg)
                            .getValue().getClass();
                } else {
                    throw new BasicInterpreterInternalError("What class is "
                            + arg);
                }
                result.add(klass);
            }
        }
        return result == null ? null : result.toArray(new Class<?>[0]);
    }

    @SuppressWarnings("unchecked")
    private static Object getParameter(RightValue arg) {
        Object object = null;
        if (arg instanceof AbstractPrimitiveRightValue<?>) {
            object = ((AbstractPrimitiveRightValue<Object>) arg).getValue();
        } else {
            throw new BasicInterpreterInternalError("What class is " + arg);
        }
        return object;
    }

    private static boolean parameterLengthMatch(Class<?>[] parameterTypes,
            List<RightValue> args) {
        if (args == null) {
            return parameterTypes.length == 0;
        }
        return parameterTypes.length == args.size();
    }

    private static Object[] getObjectArray(List<RightValue> args,
            Method method, ExtendedInterpreter extendedInterpreter)
            throws ExecutionException {
        Class<?>[] parameterTypes = method.getParameterTypes();
        // if the declaring class of the method implements the interface
        // WHATEVER //TODO find a good name
        // and the first parameter of the method is
        // com.scriptbasic.interfaces.interpreter then auto magically
        // pass that parameter to the method
        if (!parameterLengthMatch(parameterTypes,args)) {
            throw new BasicRuntimeException(
                    "Different number of parameters calling the Java method '"
                            + method.getName() + "'");
        }
        ArrayList<Object> result = null;
        if (args != null) {
            result = new ArrayList<>();
            int parameterIndex = 0;
            for (RightValue arg : args) {
                Object object = CastUtility.cast(getParameter(arg),
                        parameterTypes[parameterIndex]);
                result.add(object);
                parameterIndex++;
            }
        }
        return result == null ? null : result.toArray(new Object[0]);
    }

    private RightValue callMethod(
            final ExtendedInterpreter extendedInterpreter, final Object object,
            final Class<?> klass) throws ExecutionException {
        RightValue result = null;
        FunctionCall rightOp = (FunctionCall) getRightOperand();
        String methodName = rightOp.getVariableName();
        ExpressionList expressionList = rightOp.getExpressionList();
        List<RightValue> args = evaluateExpressionList(extendedInterpreter,
                expressionList);
        Method method = null;
        final Class<?> calculatedKlass = klass == null ? object.getClass()
                : klass;
        method = extendedInterpreter.getJavaMethod(calculatedKlass, methodName);
        if (method == null) {
            try {
                method = calculatedKlass.getMethod(methodName,
                        getClassArray(args));
            } catch (NoSuchMethodException | SecurityException e) {
                throw new BasicRuntimeException("Method '" + methodName
                        + "' from class '" + klass + "' can not be accessed", e);
            }
        }
        Object methodResultObject = null;
        try {
            methodResultObject = method.invoke(object,
                    getObjectArray(args, method, extendedInterpreter));
        } catch (IllegalAccessException | IllegalArgumentException
                | InvocationTargetException e) {
            throw new BasicRuntimeException("Can not invoke method "
                    + methodName, e);
        } catch (Throwable e) {// TODO examine later what to catch...
            throw new BasicRuntimeException("Invoking methos '" + methodName
                    + "' throws exception:", e);
        }
        result = RightValueUtils.createRightValue(methodResultObject);
        return result;
    }

    @SuppressWarnings("unchecked")
    private Object getLeftOperandObject(ExtendedInterpreter extendedInterpreter)
            throws ExecutionException {
        RightValue leftOp = getLeftOperand().evaluate(extendedInterpreter);
        if (!(leftOp instanceof AbstractPrimitiveRightValue<?>)) {
            throw new BasicRuntimeException("Can not get field access from "
                    + (leftOp == null ? "null" : leftOp.getClass())
                    + " from variable "
                    + ((VariableAccess) getLeftOperand()).getVariableName());
        }
        return ((AbstractPrimitiveRightValue<Object>) leftOp).getValue();
    }

    private Class<?> getStaticClass(ExtendedInterpreter extendedInterpreter) {
        Class<?> result = null;
        if (getLeftOperand() instanceof VariableAccess) {
            String classAsName = ((VariableAccess) getLeftOperand())
                    .getVariableName();
            if (extendedInterpreter.getUseMap().containsKey(classAsName)) {
                result = extendedInterpreter.getUseMap().get(classAsName);
            }
        }
        return result;
    }

    @Override
    public RightValue evaluate(ExtendedInterpreter extendedInterpreter)
            throws ExecutionException {
        RightValue result = null;
        Expression rightOp = getRightOperand();

        if (rightOp instanceof VariableAccess) {

            result = fetchField(extendedInterpreter);

        } else if (rightOp instanceof FunctionCall) {
            Class<?> klass = getStaticClass(extendedInterpreter);
            Object object = null;
            if (klass == null) {
                object = getLeftOperandObject(extendedInterpreter);
            }
            result = callMethod(extendedInterpreter, object, klass);

        } else if (rightOp instanceof ArrayElementAccess) {
            // TODO develop this
            throw new BasicRuntimeException(
                    "Field access operator is not implemented to handle array element access (yet).");
        } else {
            throw new BasicRuntimeException(
                    "Field access operator is not implemented to handle variable field.");
        }
        return result;
    }
}

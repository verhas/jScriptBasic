package com.scriptbasic.executors.operators;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import com.scriptbasic.executors.AbstractIdentifieredExpression;
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
import com.scriptbasic.utility.KlassUtility;
import com.scriptbasic.utility.RightValueUtility;

/**
 * This is the highest priority operator (priority 1) that is used to access a
 * field of an object. This operator is the (.) dot operator.
 * 
 * @author Peter Verhas
 * 
 */
public class ObjectFieldAccessOperator extends AbstractBinaryOperator {

    private Object fetchFieldObject(ExtendedInterpreter extendedInterpreter)
            throws ExecutionException {
        Object object = getLeftOperandObject(extendedInterpreter);
        AbstractIdentifieredExpression rightOp = (AbstractIdentifieredExpression) getRightOperand();
        String fieldName = rightOp.getVariableName();
        Object fieldObject = KlassUtility.getField(object, fieldName);
        return fieldObject;
    }

    private RightValue fetchField(ExtendedInterpreter extendedInterpreter)
            throws ExecutionException {
        Object fieldObject = fetchFieldObject(extendedInterpreter);
        RightValue rightValue = RightValueUtility.createRightValue(fieldObject);
        return rightValue;
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

    private static Class<?>[] getClassArray(List<RightValue> args) {
        ArrayList<Class<?>> result = null;
        if (args != null) {
            result = new ArrayList<>();
            for (RightValue arg : args) {
                result.add(RightValueUtility.getValueObject(arg).getClass());
            }
        }
        return result == null ? null : result.toArray(new Class<?>[0]);
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
        ArrayList<Object> result = null;
        if (args != null) {
            result = new ArrayList<>();
            int parameterIndex = 0;
            for (RightValue arg : args) {
                Object object = CastUtility.cast(
                        RightValueUtility.getValueObject(arg),
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
        } catch (Exception e) {
            throw new BasicRuntimeException("Invoking methos '" + methodName
                    + "' throws exception:", e);
        }
        result = RightValueUtility.createRightValue(methodResultObject);
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

    private static Object getArrayElement(Object[] array, Integer index)
            throws ExecutionException {
        if (index < 0) {
            throw new BasicRuntimeException("Can not use " + index
                    + " < 0 as array index");
        }
        if (index >= array.length) {
            throw new BasicRuntimeException("Cann not use index" + index
                    + " > max index" + (array.length - 1) + " ");
        }
        return array[index];
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
            Object variable = fetchFieldObject(extendedInterpreter);
            for (Expression expression : ((ArrayElementAccess) rightOp)
                    .getExpressionList()) {
                if (variable instanceof Object[]) {
                    Integer index = RightValueUtility
                            .convert2Integer(expression
                                    .evaluate(extendedInterpreter));
                    variable = getArrayElement((Object[]) variable, index);
                } else {
                    throw new BasicRuntimeException(
                            "Java object field is not array, can not access it that way.");
                }
            }
            result = RightValueUtility.createRightValue(variable);
        } else {
            throw new BasicRuntimeException(
                    "Field access operator is not implemented to handle variable field.");
        }
        return result;
    }
}

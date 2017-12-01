package com.scriptbasic.executors.operators;

import com.scriptbasic.executors.AbstractIdentifieredExpression;
import com.scriptbasic.executors.rightvalues.AbstractPrimitiveRightValue;
import com.scriptbasic.executors.rightvalues.ArrayElementAccess;
import com.scriptbasic.executors.rightvalues.FunctionCall;
import com.scriptbasic.executors.rightvalues.VariableAccess;
import com.scriptbasic.interfaces.*;
import com.scriptbasic.utility.ExpressionUtility;
import com.scriptbasic.utility.KlassUtility;
import com.scriptbasic.utility.ReflectionUtility;
import com.scriptbasic.utility.RightValueUtility;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * This is the highest priority operator (priority 1) that is used to access a
 * field of an object. This operator is the (.) dot operator.
 *
 * @author Peter Verhas
 */
public class JavaObjectFieldAccessOperator extends AbstractBinaryOperator {

    private static Class<?>[] getClassArray(final List<RightValue> args) {
        final ArrayList<Class<?>> result = new ArrayList<>();
        if (args != null) {
            for (final RightValue arg : args) {
                result.add(RightValueUtility.getValueObject(arg).getClass());
            }
        }
        return result.isEmpty() ? new Class<?>[0] : result.toArray(new Class<?>[0]);
    }

    private static Object getArrayElement(final Object[] array, final Integer index)
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

    private Object fetchFieldObject(final ExtendedInterpreter extendedInterpreter)
            throws ExecutionException {
        final Object object = getLeftOperandObject(extendedInterpreter);
        final AbstractIdentifieredExpression rightOp = (AbstractIdentifieredExpression) getRightOperand();
        final String fieldName = rightOp.getVariableName();
        return KlassUtility.getField(object, fieldName);
    }

    private RightValue fetchField(final ExtendedInterpreter extendedInterpreter)
            throws ExecutionException {
        final Object fieldObject = fetchFieldObject(extendedInterpreter);
        return RightValueUtility.createRightValue(fieldObject);
    }

    private RightValue callMethod(final ExtendedInterpreter interpreter,
                                  final Object object, final Class<?> klass)
            throws ExecutionException {
        RightValue result = null;
        final FunctionCall rightOp = (FunctionCall) getRightOperand();
        final String methodName = rightOp.getVariableName();
        final ExpressionList expressionList = rightOp.getExpressionList();
        final List<RightValue> args = ExpressionUtility.evaluateExpressionList(
                interpreter, expressionList);
        final Class<?> calculatedKlass = klass == null ? object.getClass()
                : klass;
        Method method = interpreter.getJavaMethod(calculatedKlass, methodName);
        if (method == null) {
            try {
                method = findAppropriateMethod(calculatedKlass, methodName, args);
            } catch (final NoSuchMethodException e) {
                throw new BasicRuntimeException("Method '" + methodName
                        + "' from class '" + klass + "' can not be accessed", e);
            }
        }
        Object methodResultObject = null;
        methodResultObject = ReflectionUtility.invoke(methodName, interpreter,
                method, object, args);

        result = RightValueUtility.createRightValue(methodResultObject);
        return result;
    }

    /**
     * Find a method that can be called from the BASIC program with the name as specified in the BASIC program
     * and with the arguments specified in the BASIC program.
     * <p>
     * The code tries to find all methods that have the given name, the same or more number of arguments and the
     * arguments can be assigned from the values of the BASIC call. If there are more arguments then they will be
     * null during the call, so it is checked that these arguments are not of primitive type.
     * <p>
     * If there are more than one such method then the one given by the reflection call
     * {@link Class#getMethod(String, Class[])}
     * returns. This requires exact match of the arguments.
     *
     * @param klass      the class in which we look for the method
     * @param methodName the name of the method
     * @param args       the arguments we want to pass to the method
     * @return the method that matches the name and the argument types
     * @throws BasicRuntimeException if there is no method with that name
     * @throws NoSuchMethodException if there is more than one method with name and matching argument
     *                               but none of them is exact match.
     */
    private Method findAppropriateMethod(final Class<?> klass, final String methodName, final List<RightValue> args) throws BasicRuntimeException, NoSuchMethodException {
        final Class<?>[] argClasses = getClassArray(args);
        final List<Method> methods = Arrays.stream(klass.getMethods()).filter(method -> method.getName().equals(methodName))
                .filter(method -> method.getParameterTypes().length >= argClasses.length)
                .filter(method ->
                        !IntStream.range(0, method.getParameterTypes().length - 1)
                                .anyMatch(i -> i < argClasses.length ? !method.getParameterTypes()[i].isAssignableFrom(argClasses[i])
                                        : method.getParameterTypes()[i].isPrimitive())
                ).collect(Collectors.toList());
        if (methods.size() > 1) {
            return klass.getMethod(methodName, argClasses);
        }
        if (methods.size() == 0) {
            throw new BasicRuntimeException("There is no matching method for '" + methodName + "' in class '" + klass.getName() + "'");
        }
        return methods.get(0);
    }

    @SuppressWarnings("unchecked")
    private Object getLeftOperandObject(final ExtendedInterpreter extendedInterpreter)
            throws ExecutionException {
        final RightValue leftOp = getLeftOperand().evaluate(extendedInterpreter);
        if (!(leftOp instanceof AbstractPrimitiveRightValue<?>)) {
            throw new BasicRuntimeException("Can not get field access from "
                    + (leftOp == null ? "null" : leftOp.getClass())
                    + " from variable "
                    + ((VariableAccess) getLeftOperand()).getVariableName());
        }
        return ((AbstractPrimitiveRightValue<Object>) leftOp).getValue();
    }

    private Class<?> getStaticClass(final ExtendedInterpreter interpreter) {
        Class<?> result = null;
        if (getLeftOperand() instanceof VariableAccess) {
            final String classAsName = ((VariableAccess) getLeftOperand())
                    .getVariableName();
            if (interpreter.getUseMap().containsKey(classAsName)) {
                result = interpreter.getUseMap().get(classAsName);
            }
        }
        return result;
    }

    @Override
    public RightValue evaluate(final ExtendedInterpreter interpreter)
            throws ExecutionException {
        RightValue result = null;
        final Expression rightOp = getRightOperand();

        if (rightOp instanceof VariableAccess) {

            result = fetchField(interpreter);

        } else if (rightOp instanceof FunctionCall) {
            final Class<?> klass = getStaticClass(interpreter);
            Object object = null;
            if (klass == null) {
                object = getLeftOperandObject(interpreter);
            }
            result = callMethod(interpreter, object, klass);

        } else if (rightOp instanceof ArrayElementAccess) {
            Object variable = fetchFieldObject(interpreter);
            for (final Expression expression : ((ArrayElementAccess) rightOp)
                    .getExpressionList()) {
                if (variable instanceof Object[]) {
                    final Integer index = RightValueUtility
                            .convert2Integer(expression.evaluate(interpreter));
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

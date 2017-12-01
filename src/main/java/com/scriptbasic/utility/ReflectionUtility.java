package com.scriptbasic.utility;

import com.scriptbasic.interfaces.BasicRuntimeException;
import com.scriptbasic.executors.rightvalues.BasicArrayValue;
import com.scriptbasic.interfaces.*;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

/**
 * @author Peter Verhas date Aug 2, 2012
 */
public class ReflectionUtility {
    private ReflectionUtility() {
        NoInstance.isPossible();
    }

    /**
     * Invoke the {@code method} on the {@code object} using the {@code args}.
     * <p>
     * If {@code object} is {@code null} then call the static method.
     * <p>
     * <p>
     * If {@code object} is of a type that implements the interface {@link NoAccess} then the call will fail.
     * <p>
     * Before the Java method call the hook method {@code beforeCallJavaFunction} is called.
     * <p>
     * After the Java method call the hook method{@code afterCallJavaFunction} is called.
     *
     * @param interpreter the interpreter
     * @param method      the method to call
     * @param object      the object on which the call is to be performed or
     *                    {@code null} in case the method is static
     * @param args        the arguments to the method call
     * @return the object returned by the Java method if any.
     * @throws BasicRuntimeException
     */
    public static Object invoke(String symbolicName,
                                ExtendedInterpreter interpreter,
                                Method method,
                                Object object,
                                List<RightValue> args)
            throws BasicRuntimeException {
        if (object != null && object instanceof NoAccess) {
            final Object target = object instanceof NoAccessProxy ? ((NoAccessProxy) object).target : object;
            throw new BasicRuntimeException("It is not allowed to call  '" +
                    symbolicName +
                    "' on object of class '" +
                    target.getClass().getName());
        }
        interpreter.getHook().beforeCallJavaFunction(method);
        final Object javaCallResult;
        try {
            Object[] argArray = ExpressionUtility.getObjectArray(args, method,
                    interpreter);
            javaCallResult = method.invoke(object, argArray);
            if (javaCallResult instanceof BasicArrayValue) {
                ((BasicArrayValue) javaCallResult).setInterpreter(interpreter);
            }
        } catch (InvocationTargetException | IllegalArgumentException | IllegalAccessException e) {
            throw new BasicRuntimeException("Can not invoke method " + symbolicName, e);
        } catch (Exception e) {
            throw new BasicRuntimeException("Invoking method '" + symbolicName + "' throws exception:", e);
        }
        return interpreter.getHook().afterCallJavaFunction(method, javaCallResult);
    }
}

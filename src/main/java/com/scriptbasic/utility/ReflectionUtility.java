package com.scriptbasic.utility;

import com.scriptbasic.spi.Interpreter;
import com.scriptbasic.spi.RightValue;
import com.scriptbasic.api.ScriptBasicException;
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
     * @throws ScriptBasicException
     */
    public static Object invoke(final String symbolicName,
                                final Interpreter interpreter,
                                final Method method,
                                final Object object,
                                final List<RightValue> args)
            throws ScriptBasicException {
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
            final Object[] argArray = ExpressionUtility.getObjectArray(args, method,
                    interpreter);
            javaCallResult = method.invoke(object, argArray);
            setTheInterpreterIfTheResultIsBasicArray(javaCallResult, interpreter);
        } catch (InvocationTargetException e) {
            if( e.getTargetException() instanceof ScriptBasicException ){
                throw (ScriptBasicException)e.getTargetException();
            }else{
                throw new ScriptBasicException(e.getTargetException());
            }
        } catch (IllegalArgumentException | IllegalAccessException e) {
            throw new BasicRuntimeException("Can not invoke method " + symbolicName, e);
        } catch (final Exception e) {
            throw new BasicRuntimeException("Invoking method '" + symbolicName + "' throws exception:", e);
        }
        return interpreter.getHook().afterCallJavaFunction(method, javaCallResult);
    }

    private static void setTheInterpreterIfTheResultIsBasicArray(Object javaCallResult, Interpreter interpreter) {
        if (javaCallResult instanceof BasicArrayValue) {
            ((BasicArrayValue) javaCallResult).setInterpreter(interpreter);
        }
    }
}

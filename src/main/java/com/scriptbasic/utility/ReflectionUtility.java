package com.scriptbasic.utility;

import com.scriptbasic.api.ScriptBasicException;
import com.scriptbasic.executors.rightvalues.BasicArrayValue;
import com.scriptbasic.interfaces.BasicRuntimeException;
import com.scriptbasic.spi.Interpreter;
import com.scriptbasic.spi.NoAccess;
import com.scriptbasic.spi.NoAccessProxy;
import com.scriptbasic.spi.RightValue;

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
     * If {@code object} is of a type that implements the interface {@link NoAccess} then the call will fail.
     * <p>
     * Before the Java method call the hook method {@code beforeCallJavaFunction} is called.
     * <p>
     * After the Java method call the hook method{@code afterCallJavaFunction} is called.
     *
     * @param interpreter  the interpreter
     * @param symbolicName the symbolic name of the method, as it is used in the BASIC level. This is used only for
     *                     error reporting.
     * @param method       the method to call
     * @param object       the object on which the call is to be performed or
     *                     {@code null} in case the method is static
     * @param args         the arguments to the method call
     * @return the object returned by the Java method if any.
     * @throws ScriptBasicException in case of exception
     */
    public static Object invoke(final Interpreter interpreter,
                                final String symbolicName,
                                final Method method,
                                final Object object,
                                final List<RightValue> args)
            throws ScriptBasicException {
        assertNoAccess(symbolicName, object);
        interpreter.getHook().beforeCallJavaFunction(method);
        final Object javaCallResult;
        try {
            final Object[] argArray = ExpressionUtility.getObjectArray(args, method,
                    interpreter);
            javaCallResult = method.invoke(object, argArray);
            setTheInterpreterIfTheResultIsBasicArray(javaCallResult, interpreter);
        } catch (final InvocationTargetException e) {
            throw exceptionFrom(e);
        } catch (final IllegalArgumentException | IllegalAccessException e) {
            throw new BasicRuntimeException("Can not invoke method " + symbolicName, e);
        } catch (final Exception e) {
            throw new BasicRuntimeException("Invoking method '" + symbolicName + "' throws exception:", e);
        }
        return interpreter.getHook().afterCallJavaFunction(method, javaCallResult);
    }

    /**
     * Throw a ScriptBasicException enclosing the target exception (the exception that was thrown by the
     * reflectively called method) or if that exception was already a ScriptBasicException then throw
     * that one without any wrapping.
     * <p>
     * Unwrap the {@link InvocationTargetException} and wrap it into a {@link ScriptBasicException} unless it is
     * already one.
     *
     * @param e the exception that was caught during the reflective call.
     * @return a new ScriptBasicException
     */
    private static ScriptBasicException exceptionFrom(final InvocationTargetException e) {
        if (e.getTargetException() instanceof ScriptBasicException) {
            return (ScriptBasicException) e.getTargetException();
        } else {
            return new ScriptBasicException(e.getTargetException());
        }
    }

    private static void assertNoAccess(final String symbolicName, final Object object) throws BasicRuntimeException {
        if (object instanceof NoAccess) {
            final var target = object instanceof NoAccessProxy ? ((NoAccessProxy) object).target : object;
            throw new BasicRuntimeException("It is not allowed to call  '" +
                    symbolicName +
                    "' on object of class '" +
                    target.getClass().getName());
        }
    }

    private static void setTheInterpreterIfTheResultIsBasicArray(final Object javaCallResult, final Interpreter interpreter) {
        if (javaCallResult instanceof BasicArrayValue) {
            ((BasicArrayValue) javaCallResult).setInterpreter(interpreter);
        }
    }
}

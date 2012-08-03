/**
 * 
 */
package com.scriptbasic.hooks;

import java.lang.reflect.Method;

import com.scriptbasic.interfaces.Command;
import com.scriptbasic.interfaces.Interpreter;
import com.scriptbasic.interfaces.InterpreterHook;
import com.scriptbasic.interfaces.LeftValueList;
import com.scriptbasic.interfaces.RightValue;

/**
 * A simple implementation of the InterpreterHook.
 * <p>
 * This hook does nothing, only calls the next in the hook chain. Classes
 * implementing hooks may use the pleasant feature of this hook that it
 * implements empty methods for each hook method of the interface. For example
 * the method {@code beforeExecute} calls the method {@code beforeExecuteEx} and
 * then it calls on the chain. A hook extending this class instead of
 * implementing the interface need not implement hook methods that are empty and
 * need not care about not to break the chain.
 * 
 * @author Peter Verhas
 * @date Aug 3, 2012
 * 
 */
public class SimpleHook implements InterpreterHook {

    private InterpreterHook next;
    private Interpreter interpreter;

    /**
     * @return the interpreter
     */
    protected Interpreter getInterpreter() {
        return interpreter;
    }

    /**
     * @param interpreter
     *            the interpreter to set
     */
    @Override
    public void setInterpreter(Interpreter interpreter) {
        this.interpreter = interpreter;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.scriptbasic.interfaces.InterpreterHook#setNext(com.scriptbasic.interfaces
     * .InterpreterHook)
     */
    @Override
    public void setNext(InterpreterHook next) {
        this.next = next;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.scriptbasic.interfaces.InterpreterHook#beforeExecute(com.scriptbasic
     * .interfaces.Command)
     */
    @Override
    public void beforeExecute(Command command) {
        beforeExecuteEx(command);
        if (next != null) {
            next.beforeExecute(command);
        }
    }

    /**
     * @param command
     */
    public void beforeExecuteEx(Command command) {
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.scriptbasic.interfaces.InterpreterHook#afterExecute(com.scriptbasic
     * .interfaces.Command)
     */
    @Override
    public void afterExecute(Command command) {
        afterExecuteEx(command);
        if (next != null) {
            next.afterExecute(command);
        }

    }

    /**
     * @param command
     */
    public void afterExecuteEx(Command command) {
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.scriptbasic.interfaces.InterpreterHook#beforeRegisteringJavaMethod
     * (java.lang.String, java.lang.Class, java.lang.String,
     * java.lang.Class<?>[])
     */
    @Override
    public void beforeRegisteringJavaMethod(String alias, Class<?> klass,
            String methodName, Class<?>[] argumentTypes) {
        beforeRegisteringJavaMethodEx(alias, klass, methodName, argumentTypes);
        if (next != null) {
            next.beforeRegisteringJavaMethod(alias, klass, methodName,
                    argumentTypes);
        }

    }

    /**
     * @param alias
     * @param klass
     * @param methodName
     * @param argumentTypes
     */
    public void beforeRegisteringJavaMethodEx(String alias, Class<?> klass,
            String methodName, Class<?>[] argumentTypes) {
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.scriptbasic.interfaces.InterpreterHook#beforePush(com.scriptbasic
     * .interfaces.Command)
     */
    @Override
    public void beforePush(Command command) {
        beforePushEx(command);
        if (next != null) {
            next.beforePush(command);
        }
    }

    /**
     * @param command
     */
    public void beforePushEx(Command command) {
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.scriptbasic.interfaces.InterpreterHook#afterPush(com.scriptbasic.
     * interfaces.Command)
     */
    @Override
    public void afterPush(Command command) {
        afterPushEx(command);
        if (next != null) {
            next.afterPush(command);
        }
    }

    /**
     * @param command
     */
    public void afterPushEx(Command command) {
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.scriptbasic.interfaces.InterpreterHook#beforePop()
     */
    @Override
    public void beforePop() {
        beforePopEx();
        if (next != null) {
            next.beforePop();
        }
    }

    /**
     * 
     */
    public void beforePopEx() {
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.scriptbasic.interfaces.InterpreterHook#afterPop(com.scriptbasic.
     * interfaces.Command)
     */
    @Override
    public void afterPop(Command command) {
        afterPopEx(command);
        if (next != null) {
            next.afterPop(command);
        }
    }

    /**
     * @param command
     */
    public void afterPopEx(Command command) {
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.scriptbasic.interfaces.InterpreterHook#setReturnValue(com.scriptbasic
     * .interfaces.RightValue)
     */
    @Override
    public void setReturnValue(RightValue returnValue) {
        setReturnValueEx(returnValue);
        if (next != null) {
            next.setReturnValue(returnValue);
        }
    }

    /**
     * @param returnValue
     */
    public void setReturnValueEx(RightValue returnValue) {
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.scriptbasic.interfaces.InterpreterHook#beforeSubroutineCall(java.
     * lang.String, com.scriptbasic.interfaces.LeftValueList,
     * com.scriptbasic.interfaces.RightValue[])
     */
    @Override
    public void beforeSubroutineCall(String subroutineName,
            LeftValueList arguments, RightValue[] argumentValues) {
        beforeSubroutineCallEx(subroutineName, arguments, argumentValues);
        if (next != null) {
            next.beforeSubroutineCall(subroutineName, arguments, argumentValues);
        }
    }

    /**
     * @param subroutineName
     * @param arguments
     * @param argumentValues
     */
    public void beforeSubroutineCallEx(String subroutineName,
            LeftValueList arguments, RightValue[] argumentValues) {
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.scriptbasic.interfaces.InterpreterHook#beforeCallJavaFunction(java
     * .lang.reflect.Method)
     */
    @Override
    public void beforeCallJavaFunction(Method method) {
        beforeCallJavaFunctionEx(method);
        if (next != null) {
            next.beforeCallJavaFunction(method);
        }
    }

    /**
     * @param method
     */
    public void beforeCallJavaFunctionEx(Method method) {
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.scriptbasic.interfaces.InterpreterHook#afterCallJavaFunction(java
     * .lang.reflect.Method)
     */
    @Override
    public Object afterCallJavaFunction(Method method, final Object result) {
        Object hookedResult = afterCallJavaFunctionEx(method, result);
        if (next != null) {
            hookedResult = next.afterCallJavaFunction(method, hookedResult);
        }
        return hookedResult;
    }

    /**
     * @param method
     */
    @SuppressWarnings("static-method")
    public Object afterCallJavaFunctionEx(Method method, Object result) {
        return result;
    }

}

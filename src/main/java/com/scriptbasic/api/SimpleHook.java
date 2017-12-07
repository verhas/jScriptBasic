package com.scriptbasic.api;

import com.scriptbasic.api.InterpreterHook;
import com.scriptbasic.interfaces.*;

import java.lang.reflect.Method;

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
 * date Aug 3, 2012
 */
public abstract class SimpleHook implements InterpreterHook {

    private InterpreterHook next;
    private Interpreter interpreter;

    /**
     * @return the interpreter
     */
    protected Interpreter getInterpreter() {
        return interpreter;
    }

    /**
     * @param interpreter the interpreter to set
     */
    @Override
    public void setInterpreter(final Interpreter interpreter) {
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
    public void setNext(final InterpreterHook next) {
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
    public void beforeExecute(final Command command) {
        interpreter.disableHook();
        beforeExecuteEx(command);
        interpreter.enableHook();
        next.beforeExecute(command);
    }

    /**
     * @param command
     */
    public void beforeExecuteEx(final Command command) {
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.scriptbasic.interfaces.InterpreterHook#afterExecute(com.scriptbasic
     * .interfaces.Command)
     */
    @Override
    public void afterExecute(final Command command) {
        interpreter.disableHook();
        afterExecuteEx(command);
        interpreter.enableHook();
        next.afterExecute(command);
    }

    /**
     * @param command
     */
    public void afterExecuteEx(final Command command) {
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
    public void beforeRegisteringJavaMethod(final String alias,
                                            final Class<?> klass, final String methodName,
                                            final Class<?>[] argumentTypes) {
        interpreter.disableHook();
        beforeRegisteringJavaMethodEx(alias, klass, methodName, argumentTypes);
        interpreter.enableHook();
        next.beforeRegisteringJavaMethod(alias, klass, methodName,
                argumentTypes);

    }

    /**
     * @param alias
     * @param klass
     * @param methodName
     * @param argumentTypes
     */
    public void beforeRegisteringJavaMethodEx(final String alias,
                                              final Class<?> klass, final String methodName,
                                              final Class<?>[] argumentTypes) {
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.scriptbasic.interfaces.InterpreterHook#beforePush(com.scriptbasic
     * .interfaces.Command)
     */
    @Override
    public void beforePush(final Command command) {
        interpreter.disableHook();
        beforePushEx(command);
        interpreter.enableHook();
        next.beforePush(command);
    }

    /**
     * @param command
     */
    public void beforePushEx(final Command command) {
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.scriptbasic.interfaces.InterpreterHook#afterPush(com.scriptbasic.
     * interfaces.Command)
     */
    @Override
    public void afterPush(final Command command) {
        interpreter.disableHook();
        afterPushEx(command);
        interpreter.enableHook();
        next.afterPush(command);
    }

    /**
     * @param command
     */
    public void afterPushEx(final Command command) {
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.scriptbasic.interfaces.InterpreterHook#beforePop()
     */
    @Override
    public void beforePop() {
        interpreter.disableHook();
        beforePopEx();
        interpreter.enableHook();
        next.beforePop();
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
    public void afterPop(final Command command) {
        interpreter.disableHook();
        afterPopEx(command);
        interpreter.enableHook();
        next.afterPop(command);
    }

    /**
     * @param command
     */
    public void afterPopEx(final Command command) {
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.scriptbasic.interfaces.InterpreterHook#setReturnValue(com.scriptbasic
     * .interfaces.RightValue)
     */
    @Override
    public void setReturnValue(final RightValue returnValue) {
        interpreter.disableHook();
        setReturnValueEx(returnValue);
        interpreter.enableHook();
        next.setReturnValue(returnValue);
    }

    /**
     * @param returnValue
     */
    public void setReturnValueEx(final RightValue returnValue) {
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
    public void beforeSubroutineCall(final String subroutineName,
                                     final LeftValueList arguments, final RightValue[] argumentValues) {
        interpreter.disableHook();
        beforeSubroutineCallEx(subroutineName, arguments, argumentValues);
        interpreter.enableHook();
        next.beforeSubroutineCall(subroutineName, arguments, argumentValues);
    }

    /**
     * @param subroutineName
     * @param arguments
     * @param argumentValues
     */
    public void beforeSubroutineCallEx(final String subroutineName,
                                       final LeftValueList arguments, final RightValue[] argumentValues) {
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.scriptbasic.interfaces.InterpreterHook#beforeCallJavaFunction(java
     * .lang.reflect.Method)
     */
    @Override
    public void beforeCallJavaFunction(final Method method) {
        interpreter.disableHook();
        beforeCallJavaFunctionEx(method);
        interpreter.enableHook();
        next.beforeCallJavaFunction(method);
    }

    /**
     * @param method
     */
    public void beforeCallJavaFunctionEx(final Method method) {
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.scriptbasic.interfaces.InterpreterHook#afterCallJavaFunction(java
     * .lang.reflect.Method)
     */
    @Override
    public Object afterCallJavaFunction(final Method method, final Object result) {
        interpreter.disableHook();
        Object hookedResult = afterCallJavaFunctionEx(method, result);
        interpreter.enableHook();
        hookedResult = next.afterCallJavaFunction(method, hookedResult);
        return hookedResult;
    }

    /**
     * @param method
     */
    public Object afterCallJavaFunctionEx(final Method method,
                                          final Object result) {
        return result;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.scriptbasic.interfaces.InterpreterHook#variableRead(java.lang.String,
     * com.scriptbasic.interfaces.RightValue)
     */
    @Override
    public RightValue variableRead(final String variableName,
                                   final RightValue value) {
        interpreter.disableHook();
        RightValue hookedValue = variableReadEx(variableName, value);
        interpreter.enableHook();
        hookedValue = next.variableRead(variableName, hookedValue);
        return hookedValue;
    }

    /**
     * @param variableName
     * @param value
     * @return
     */

    public RightValue variableReadEx(final String variableName,
                                     final RightValue value) {
        return value;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.scriptbasic.interfaces.InterpreterHook#init()
     */
    @Override
    public void init() {
        interpreter.disableHook();
        initEx();
        interpreter.enableHook();
        next.init();

    }

    /**
     *
     */
    public void initEx() {
    }

}

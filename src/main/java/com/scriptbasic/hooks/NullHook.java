package com.scriptbasic.hooks;

import com.scriptbasic.interfaces.*;

import java.lang.reflect.Method;

/**
 * The simplest implementation of the InterfaceHook interface. This implements
 * each of the methods of the interface, each doing nothing.
 * <p>
 * This hook is used to chain into the hook chain first so that hook classes
 * need not check if there is a next hook object in the chain.
 *
 * @author Peter Verhas
 * date Aug 15, 2012
 */
public class NullHook implements InterpreterHook {

    @Override
    public void init() {
    }

    @Override
    public void setNext(final InterpreterHook next) {
    }

    @Override
    public void setInterpreter(final ExtendedInterpreter interpreter) {
    }

    @Override
    public void beforeExecute(final Command command) {
    }

    @Override
    public void afterExecute(final Command command) {
    }

    @Override
    public void beforeRegisteringJavaMethod(final String alias, final Class<?> klass,
                                            final String methodName, final Class<?>[] argumentTypes) {
    }

    @Override
    public void beforePush(final Command command) {
    }

    @Override
    public void afterPush(final Command command) {
    }

    @Override
    public void beforePop() {
    }

    @Override
    public void afterPop(final Command command) {
    }

    @Override
    public void setReturnValue(final RightValue returnValue) {
    }

    @Override
    public void beforeSubroutineCall(final String subroutineName,
                                     final LeftValueList arguments, final RightValue[] argumentValues) {
    }

    @Override
    public void beforeCallJavaFunction(final Method method) {
    }

    @Override
    public Object afterCallJavaFunction(final Method method, final Object result) {
        return result;
    }

    @Override
    public RightValue variableRead(final String variableName, final RightValue value) {
        return value;
    }

}

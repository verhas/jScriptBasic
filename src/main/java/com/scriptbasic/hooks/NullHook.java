package com.scriptbasic.hooks;

import java.lang.reflect.Method;

import com.scriptbasic.interfaces.Command;
import com.scriptbasic.interfaces.ExtendedInterpreter;
import com.scriptbasic.interfaces.InterpreterHook;
import com.scriptbasic.interfaces.LeftValueList;
import com.scriptbasic.interfaces.RightValue;

/**
 * The simplest implementation of the InterfaceHook interface. This implements
 * each of the methods of the interface, each doing nothing.
 * <p>
 * This hook is used to chain into the hook chain first so that hook classes
 * need not check if there is a next hook object in the chain.
 * 
 * @author Peter Verhas
 * date Aug 15, 2012
 * 
 */
public class NullHook implements InterpreterHook {

    @Override
    public void init() {
    }

    @Override
    public void setNext(InterpreterHook next) {
    }

    @Override
    public void setInterpreter(ExtendedInterpreter interpreter) {
    }

    @Override
    public void beforeExecute(Command command) {
    }

    @Override
    public void afterExecute(Command command) {
    }

    @Override
    public void beforeRegisteringJavaMethod(String alias, Class<?> klass,
            String methodName, Class<?>[] argumentTypes) {
    }

    @Override
    public void beforePush(Command command) {
    }

    @Override
    public void afterPush(Command command) {
    }

    @Override
    public void beforePop() {
    }

    @Override
    public void afterPop(Command command) {
    }

    @Override
    public void setReturnValue(RightValue returnValue) {
    }

    @Override
    public void beforeSubroutineCall(String subroutineName,
            LeftValueList arguments, RightValue[] argumentValues) {
    }

    @Override
    public void beforeCallJavaFunction(Method method) {
    }

    @Override
    public Object afterCallJavaFunction(Method method, Object result) {
        return result;
    }

    @Override
    public RightValue variableRead(String variableName, RightValue value) {
        return value;
    }

}

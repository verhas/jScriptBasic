/**
 * 
 */
package com.scriptbasic.interfaces;

import java.lang.reflect.Method;

/**
 * An interpreter hook can be registered into an interpreter and the methods of
 * the hook are called by the interpreter when executing the the BASIC program.
 * Hooks usually are extension means to implement profilers, debuggers and so
 * on.
 * <p>
 * The hooks are in a chain and every hook method should call the same hook
 * method on the next hook object on the chain. The hooks are chained backward.
 * The last hook will be called directly by the interpreter and that hook will
 * call the previous and so on. Thus the first hook registered is called last
 * and the last registered will be called first.
 * <p>
 * A hook method can decide for some good reason to break the chain not invoking
 * the next element in the chain.
 * 
 * @author Peter Verhas
 * @date Aug 3, 2012
 * 
 */
public interface InterpreterHook {

    /**
     * When a hook is registered the registering process calls this method and
     * passes the next element in the hook chain. The hook object should
     * remember this object and call the appropriate methods when that is called
     * not to break the chain.
     * 
     * @param next
     *            the next element in the chain.
     */
    void setNext(InterpreterHook next);

    /**
     * During registration the interpreter calls this method to make the
     * interpreter accessible for the hook objects.
     * 
     * @param interpreter
     */
    void setInterpreter(Interpreter interpreter);

    /**
     * This method is called before the interpreter executes a command.
     * 
     * @param command
     *            the command object to be executed
     */
    void beforeExecute(Command command);

    /**
     * This method is called after the interpreter executed a command.
     * 
     * @param command
     *            the command just executed.
     */
    void afterExecute(Command command);

    /**
     * This method is called before registering a java method into the
     * interpreter.
     * 
     * @param alias
     * @param klass
     * @param methodName
     * @param argumentTypes
     */
    void beforeRegisteringJavaMethod(String alias, Class<?> klass,
            String methodName, Class<?>[] argumentTypes);

    /**
     * @param command
     */
    void beforePush(Command command);

    /**
     * @param command
     */
    void afterPush(Command command);

    /**
     * 
     */
    void beforePop();

    /**
     * @param command
     */
    void afterPop(Command command);

    /**
     * This method is called after a subroutine has set its return value. It is
     * possible to modify the return value calling back to the interpreter but
     * it has to be only invoked together with {@see
     * ExtendedInterpreter#disableHook()} and {@see
     * ExtendedInterpreter#enableHook()}.
     * 
     * @param returnValue
     * 
     * @see ExtendedInterpreter#disableHook()
     */
    void setReturnValue(RightValue returnValue);

    /**
     * This method is called before the interpreter invokes a subroutine. At
     * this point the local variables are those of the subroutine to be called.
     * 
     * @param subroutineName
     *            the symbolic name of the subroutine
     * @param arguments
     *            the argument left values
     * @param argumentValues
     *            the argument evaluated values that were assigned to the local
     *            variable table to the arguments
     */
    void beforeSubroutineCall(String subroutineName, LeftValueList arguments,
            RightValue[] argumentValues);

    /**
     * @param method
     */
    void beforeCallJavaFunction(Method method);

    /**
     * This method is called when the interpreter was calling a Java static
     * method. The
     * 
     * @param method
     *            the method that was called
     * @param result
     *            teh result that the static method returned
     * @return the modified result or just the same object if the hook does not
     *         want to modify the result
     */
    Object afterCallJavaFunction(Method method, Object result);
}

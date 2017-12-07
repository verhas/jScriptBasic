package com.scriptbasic.api;

import com.scriptbasic.interfaces.Interpreter;

import java.lang.reflect.Method;

/**
 * An interpreter hook can be registered into an interpreter and the methods of
 * the hook are called by the interpreter when executing the the BASIC program.
 * Hooks usually are extension means to implement profilers, debuggers and so
 * on.
 * <p>
 * To implement a hook the class should implement this interface. It is
 * recommended to extend the class {@link SimpleHook}
 * instead of implementing a fresh class just implementing this interface. There
 * are a lot of methods in this interface and usually a hook class need
 * functionality only for a subset. The class {@link
 * SimpleHook} gives default implementation for the
 * methods and eases hook development providing extended methods.
 * <p>
 * To make a hook class used by the interpreter the class should be on the
 * classpath during runtime and the configuration key {@code hook.}<i>N</i>
 * should contains the FQN of the implementing class. The configuration
 * parameter <i>N</i> should run from 0 ... and the hooks will be registered in
 * the order they are numbered and executed in backward order. To use the hook
 * delivered with jScriptBasic you should configure:
 * </p>
 * <pre>
 * hook.0=com.scriptbasic.hooks.RunLimitHook
 * </pre>
 * <p>
 * to have this hook called last (registered first). There are no hooks
 * registered automatically.
 * <p>
 * The hooks are in a chain and every hook method should call the same hook
 * method on the next hook object on the chain. The hooks are chained backward.
 * The last hook will be called directly by the interpreter and that hook will
 * call the previous and so on. Thus the first hook registered is called last
 * and the last registered will be called first.
 * <p>
 * A hook method can decide for some good reason to break the chain not invoking
 * the next element in the chain. When implementing a hook class extending the class
 * {@link SimpleHook} all methods defined in this interface
 * are implemented. They simply disable hook handling, call the method of the same
 * name with the extra 'Ex' at the end, enable hook handling and then call the next hook.
 * For example the method {@link SimpleHook#beforePop()} calls {@link SimpleHook#beforePopEx()}
 * and then {@link  SimpleHook#beforePop()} on the next hook object. During the execution
 * of {@link SimpleHook#beforePopEx()} the hook handling is disabled. The methods defined
 * only in {@link SimpleHook} are empty and do nothing.
 * <p>
 * More information about why disabling the hook handling is needed see
 * {@link #setReturnValue(RightValue)} and {@link Interpreter#disableHook()}
 *
 * @author Peter Verhas
 * date Aug 3, 2012
 */
public interface InterpreterHook {
    /**
     * This method is called at the end of the hook registering. When this
     * method is called the hook does have interpreter and the field 'next'
     * already set. The method should do its initialization and then call the
     * same method of the next hook in the chain.
     */
    void init();

    /**
     * When a hook is registered the registering process calls this method and
     * passes the next element in the hook chain. The hook object should
     * remember this object and call the appropriate methods when that is called
     * not to break the chain.
     *
     * @param next the next element in the chain.
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
     * @param command the command object to be executed
     */
    void beforeExecute(Command command);

    /**
     * This method is called after the interpreter executed a command.
     *
     * @param command the command just executed.
     */
    void afterExecute(Command command);

    /**
     * This method is called before registering a java method into the
     * interpreter.
     *
     * @param alias         the name of the function as it will be known to the BASIC
     *                      program.
     * @param klass         the Java class where the static method is.
     * @param methodName    the Java name of the method
     * @param argumentTypes the argument types of the methods. This, together with the
     *                      name of the method and the class identifies the actual method
     *                      that will be available to the BASIC programs to be called
     *                      through the name {@code alias}.
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
     * it has to be only invoked together with {@link
     * Interpreter#disableHook()} and {@link
     * Interpreter#enableHook()}.
     *
     * @param returnValue
     * @see Interpreter#disableHook()
     */
    void setReturnValue(RightValue returnValue);

    /**
     * This method is called before the interpreter invokes a subroutine. At
     * this point the local variables are those of the subroutine to be called.
     *
     * @param subroutineName the symbolic name of the subroutine
     * @param arguments      the argument left values
     * @param argumentValues the argument evaluated values that were assigned to the local
     *                       variable table to the arguments
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
     * @param method the method that was called
     * @param result the result that the static method returned
     * @return the modified result or just the same object if the hook does not
     * want to modify the result
     */
    Object afterCallJavaFunction(Method method, Object result);

    /**
     * This hook is called when the interpreter accesses a variable.
     *
     * @param variableName the name of the variable
     * @param value        the value of the variable when accessed
     * @return the value that will be used. The implementation may decide to
     * alter the value used. Returning a modified value will not,
     * however alterthe value of the variable itself.
     */
    RightValue variableRead(String variableName, RightValue value);
}

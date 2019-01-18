package com.scriptbasic.spi;

import com.scriptbasic.api.Configuration;
import com.scriptbasic.api.ScriptBasicException;
import com.scriptbasic.executors.commands.CommandSub;
import com.scriptbasic.interfaces.BasicRuntimeException;
import com.scriptbasic.interfaces.HierarchicalVariableMap;
import com.scriptbasic.interfaces.InternalInterpreter;

import java.io.Reader;
import java.io.Writer;

/**
 * An interpreter instance executes a program.
 *
 * @author Peter Verhas
 */
public interface Interpreter extends InternalInterpreter {

    void registerHook(InterpreterHook hook);

    /**
     * Execute the program.
     *
     * @throws ScriptBasicException in case of exception
     */
    void execute() throws ScriptBasicException;

    /**
     * Set the value of the variable. If the program is in a local scope and there is a variable
     * with the given name in the local scope then the value of that variable is set, even if there
     * is a global variable with that name.
     *
     * @param name  the name of the global variable
     * @param value the value to be set
     * @throws ScriptBasicException in case of exception
     */
    void setVariable(String name, Object value) throws ScriptBasicException;

    /**
     * Get the value of a variable. Since this is not a BASIC interpreter
     * method, but rather a method that helps the embedding of the interpreter
     * the returned value is a raw Java object and not a RightValue. Thus if the
     * variable value is for example a {@link com.scriptbasic.executors.rightvalues.BasicDoubleValue} then the
     * implementation will return a {@link Double}.
     *
     * @param name the name of the variable
     * @return the value of the variable
     * @throws ScriptBasicException in case of exception
     */
    Object getVariable(String name) throws ScriptBasicException;

    /**
     * Call a function defined by the program passing the objects as arguments.
     *
     * @param functionName the name of the function in the program code
     * @param arguments    the arguments to the function
     * @return the returned object, or {@code null} if the function does not
     * return value
     * @throws ScriptBasicException in case of exception
     */
    Object call(String functionName, Object[] arguments) throws ScriptBasicException;

    /**
     * Register the functions defined in the class. Functions that can be called
     * from a BASIC program but implemented in Java are static methods that are
     * registered for the interpreter. The easiest way to define these methods
     * are to create a class and annotate the methods that serve as BASIC
     * functions with the annotation {@code @BasicFunction}
     *
     * @param klass the class the defines the functions.
     */
    void registerFunctions(Class<?> klass);

    Configuration getConfiguration();

    /**
     * Execute the program starting at the command {@code startCommand}
     *
     * @param startCommand where the execution has to start
     * @throws ScriptBasicException in case of exception during the execution
     */
    void execute(Command startCommand) throws ScriptBasicException;

    /**
     * Get a subroutine by its name.
     *
     * @param name the name of the subroutine
     * @return the "SUB" command that starts the subroutine
     */
    CommandSub getSubroutine(String name);

    /**
     * Get the return value that was set by the execution of the subroutine.
     *
     * @return return value
     */
    RightValue getReturnValue();

    /**
     * Register the return value. This method is called from a subroutine.
     *
     * @param returnValue the value that the subroutine will return
     */
    void setReturnValue(RightValue returnValue);

    /**
     * Get the global variables of the program.
     *
     * @return the variables.
     */
    HierarchicalVariableMap getVariables();

    /**
     * Register a BASIC function as Java method. Java methods may be overloaded
     * but BASIC functions can not. When a BASIC program wants to use a Java
     * method it has to declare it as
     * <pre>
     * use class from package as basicClassReference
     * </pre>
     * for example
     * <pre>
     * use Math from java.lang as m
     * </pre>
     * when the method {@code sin} is used, foe example
     * <pre>
     * a = m.sin(1.0)
     * </pre>
     * the BASIC interpreter has to find the method
     * {@code java.lang.Math.sin(Double x)}. The problem is that the method does
     * not exist because the argument is not {@code Double} but rather
     * {@code double}.
     * <p>
     * To help with this situation the BASIC program should declare the Java
     * signature of the method using the BASIC command METHOD. For example:
     * <pre>
     * method sin from java.lang.Math is (double) use as sinus
     * </pre>
     * (Note that the part {@code use as ...} is optional.)
     * <p>
     * After this command is executed the interpreter will use the defined
     * signature to locate the method. You can write in the BASIC program
     * <pre>
     * a = m.sinus(1.0)
     * </pre>
     * <p>
     * {@code registerJavaMethod()} registers the basic function alias, class,
     * java method name and the argument types so that later call to {@link
     * #getJavaMethod(Class, String)} can find the appropriate method.
     *
     * @param alias         the alias how the function will be named in basic
     * @param klass         the class where the static method is
     * @param methodName    the java name of the method
     * @param argumentTypes the types of the arguments to be used to help to identify overloaded methods
     * @throws BasicRuntimeException in case of exception
     */
    void registerJavaMethod(String alias, Class<?> klass, String methodName,
                            Class<?>[] argumentTypes) throws BasicRuntimeException;

    Reader getInput();

    /**
     * @param reader parameter
     * @see javax.script.ScriptContext#setReader(Reader)
     */
    void setInput(Reader reader);

    Writer getOutput();

    /**
     * @param writer parameter
     * @see javax.script.ScriptContext#setWriter(Writer)
     */
    void setOutput(Writer writer);

    /**
     * {@link javax.script.ScriptContext#getReader()}
     *
     * @return return value
     */
    Writer getErrorOutput();

    /**
     * @param writer parameter
     * @see javax.script.ScriptContext#setErrorWriter(Writer)
     */
    void setErrorOutput(Writer writer);

    /**
     * Temporarily disable the hooks. Following this call the hooks will not be
     * called until the {@link #enableHook()} is called.
     * <p>
     * Hook disabling was designed with the special case in mind when a hook
     * wants to alter the return value returned from a subroutine. To do so the
     * hook method has to invoke the {@link
     * Interpreter#setReturnValue(RightValue)} method, which was
     * actually calling the hook. To avoid the infinite loop and not to confuse
     * the other hook methods that are in the list sooner the hook method {@link
     * InterpreterHook#setReturnValue(RightValue)} should first disable the hook
     * mechanism, call back to the interpreter object and the enable the hook
     * mechanism again.
     */
    void disableHook();

    /**
     * Enable the hook calls. This method has to be called after the call to
     * {@link #disableHook()} to enable again the calling mechanism.
     */
    void enableHook();

    /**
     * Get the hook object the interpreter has.
     *
     * @return return value
     */
    InterpreterHook getHook();
}

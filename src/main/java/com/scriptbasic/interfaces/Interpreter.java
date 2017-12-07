package com.scriptbasic.interfaces;

import com.scriptbasic.api.Command;
import com.scriptbasic.api.InterpreterHook;
import com.scriptbasic.api.RightValue;
import com.scriptbasic.api.ScriptBasicException;
import com.scriptbasic.executors.commands.CommandSub;

import java.io.Reader;
import java.io.Writer;
import java.lang.reflect.Method;
import java.util.Map;

/**
 * An interpreter instance executes a program.
 *
 * @author Peter Verhas
 */
public interface Interpreter {

    void registerHook(InterpreterHook hook);

    /**
     * Execute the program.
     */
    void execute() throws ScriptBasicException;

    /**
     * Set the value of the global variable.
     *
     * @param name  the name of the global variable
     * @param value the value to be set
     */
    void setVariable(String name, Object value) throws ScriptBasicException;

    /**
     * Get the value of a global variable. Since this is not a BASIC interpreter
     * method, but rather a method that helps the embedding of the interpreter
     * the returned value is a raw Java object and not a RightValue. Thus if the
     * variable value is for example a {@link com.scriptbasic.executors.rightvalues.BasicDoubleValue} then the
     * implementation should return a {@link Double}.
     *
     * @param name the name of the variable
     * @return the value of the variable
     */
    Object getVariable(String name) throws ScriptBasicException;

    /**
     * Call a function defined by the program passing the objects as arguments.
     *
     * @param functionName the name of the function in the program code
     * @param arguments    the arguments to the function
     * @return the returned object, or {@code null} if the function does not
     * return value
     */
    Object call(String functionName, Object[] arguments) throws ScriptBasicException;

    /**
     * @param reader
     * @see javax.script.ScriptContext#setReader(Reader)
     */
    void setInput(Reader reader);

    /**
     * @param writer
     * @see javax.script.ScriptContext#setWriter(Writer)
     */
    void setOutput(Writer writer);

    /**
     * @param writer
     * @see javax.script.ScriptContext#setErrorWriter(Writer)
     */
    void setError(Writer writer);

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
     * @return the program that the interpreter is executing.
     */
    BuildableProgram getProgram();

    /**
     * Set the program to execute.
     *
     * @param buildableProgram
     */
    void setProgram(BuildableProgram buildableProgram);

    /**
     * Execute the program starting at the command {@code startCommand}
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
     * @return
     */
    RightValue getReturnValue();

    /**
     * Register the return value. This method is called from a subroutine.
     *
     * @param returnValue the value that the subroutine will return
     */
    void setReturnValue(RightValue returnValue);

    /**
     * Push a command to the stack. This is called before a subroutine call is
     * executed. Even though the actual stack is NOT maintained here but rather
     * in the JVM call stack, since function call is implemented using recursive
     * calls to the interpreter the call to this push and pop is vital to keep
     * track the stack trace for debugging purposes and to have a mean to limit
     * the stack size.
     * <p>
     * Calling this method also starts a new local variable frame, thus
     * evaluation of the actual argument values in a function call has to be
     * executed before calling this method.
     *
     * @param command the command from which a subroutine call was executed.
     */
    void push(Command command);

    /**
     * Same as {@link #push(Command)} and pushes the currently executing command
     * on the stack.
     */
    void push();

    /**
     * Pop the command from the top of the stack and also drop the last local
     * variables frame.
     */
    Command pop();

    /**
     * Tell the interpreter that the next command to call is not the one that
     * follows the actual command but rather the one specified by the argument.
     *
     * @param nextCommand is the next command to execute after the current command
     */
    void setNextCommand(Command nextCommand);

    /**
     * Get the command that is currently executing. This method is available for
     * the code that are executing under some command. Typically a function call
     * needs to know what command to push onto the call stack.
     *
     * @return the currently executing command
     */
    Command getCurrentCommand();

    /**
     * Get the global variables of the program.
     *
     * @return the variables.
     */
    HierarchicalVariableMap getVariables();

    /**
     * Since the Command objects should not contain runtime information there is
     * a need sometime to store information that is runtime. This method returns
     * a map that is unique to the currently executing Command and is also
     * unique to the Interpreter. Thus if two interpreters simultaneously
     * execute the same command they will return two different maps.
     * <p>
     * The interpreter initializes the map when the command asks for it the
     * first time. The life time of the map is the same as the life time of the
     * interpreter. Thus when a program finishes the map is still available to
     * the command when the Interface 'call' method is called or the interpreter
     * is restarted.
     * <p>
     * The interpreter does not alter the map in any other way than initializing
     * it to some map implementation containing initially no element.
     *
     * @return the map
     */
    Map<String, Object> getMap();

    /**
     * Programs can access Java static methods from different packages. To do
     * that the programs have to recognize when the call 'out' to Java instead
     * of looking for an internal function implemented in the program. For
     * example the BASIC program has to declare the use of these packages using
     * the command USE. For example
     * <p>
     * <pre>
     * use Math from java.lang as m
     * </pre>
     * <p>
     * (the part following the keyword 'as' is optional, in which case the Java
     * name of the class is used). After this statement is executed the use map
     * will contain the class {@code javal.lang.Math} for the key {@code m}.
     *
     * @return the use map itself.
     */
    Map<String, Class<?>> getUseMap();

    /**
     * Register a BASIC function as Java method. Java methods may be overloaded
     * but BASIC functions can not. When a BASIC program wants to use a Java
     * method it has to declare it as
     * <p>
     * <pre>
     * use class from package as basicClassReference
     * </pre>
     * <p>
     * for example
     * <p>
     * <pre>
     * use Math from java.lang as m
     * </pre>
     * <p>
     * when the method {@code sin} is used, foe example
     * <p>
     * <pre>
     * a = m.sin(1.0)
     * </pre>
     * <p>
     * the BASIC interpreter has to find the method
     * {@code java.lang.Math.sin(Double x)}. The problem is that the method does
     * not exist because the argument is not {@code Double} but rather
     * {@code double}.
     * <p>
     * To help with this situation the BASIC program should declare the Java
     * signature of the method using the BASIC command METHOD. For example:
     * <p>
     * <pre>
     * method sin from java.lang.Math is (double) use as sinus
     * </pre>
     * <p>
     * (Note that the part {@code use as ...} is optional.)
     * <p>
     * After this command is executed the interpreter will use the defined
     * signature to locate the method. You can write in the BASIC program
     * <p>
     * <pre>
     * a = m.sinus(1.0)
     * </pre>
     * <p>
     * {@code registerJavaMethod()} registers the basic function alias, class,
     * java method name and the argument types so that later call to {@link
     * #getJavaMethod(Class, String)} can find the appropriate method.
     *
     * @param klass
     * @param methodName
     * @param argumentTypes
     * @throws BasicRuntimeException
     */
    void registerJavaMethod(String alias, Class<?> klass, String methodName,
                            Class<?>[] argumentTypes) throws BasicRuntimeException;

    /**
     * Get the method named from the klass based on the declaration given in a
     * previously executed {@code METHOD} basic command. The basic command
     * METHOD has the form (example follows):
     * <p>
     * <pre>
     * method sin from java.lang.Math is (double)
     * </pre>
     * <p>
     * that defines that the method {@code sin} is in the class
     * {@code java.lang.Math} and accepts one argument, which is {@code double}
     *
     * @param klass
     * @param methodName
     * @return
     * @throws com.scriptbasic.api.ScriptBasicException
     */
    Method getJavaMethod(Class<?> klass, String methodName)
            throws ScriptBasicException;

    /**
     * {@link javax.script.ScriptContext#getReader()}
     */
    Reader getReader();

    /**
     * {@link javax.script.ScriptContext#getWriter()}
     */
    Writer getWriter();

    /**
     * {@link javax.script.ScriptContext#getReader()}
     *
     * @return
     */
    Writer getErrorWriter();

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
     * @return
     */
    InterpreterHook getHook();
}

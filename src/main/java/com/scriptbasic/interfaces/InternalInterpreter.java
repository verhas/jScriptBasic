package com.scriptbasic.interfaces;

import com.scriptbasic.api.ScriptBasicException;
import com.scriptbasic.spi.Command;

import java.lang.reflect.Method;
import java.util.Map;

/**
 * An interpreter instance executes a program.
 *
 * @author Peter Verhas
 */
public interface InternalInterpreter {

    /**
     * @return the program that the interpreter is executing.
     */
    BuildableProgram getProgram();

    /**
     * Set the program to execute.
     *
     * @param buildableProgram parameter
     */
    void setProgram(BuildableProgram buildableProgram);

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
     *
     * @return the value
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
     * <pre>
     * use Math from java.lang as m
     * </pre>
     * (the part following the keyword 'as' is optional, in which case the Java
     * name of the class is used). After this statement is executed the use map
     * will contain the class {@code javal.lang.Math} for the key {@code m}.
     *
     * @return the use map itself.
     */
    Map<String, Class<?>> getUseMap();

    /**
     * Get the method named from the klass based on the declaration given in a
     * previously executed {@code METHOD} basic command. The basic command
     * METHOD has the form (example follows):
     * <pre>
     * method sin from java.lang.Math is (double)
     * </pre>
     * that defines that the method {@code sin} is in the class
     * {@code java.lang.Math} and accepts one argument, which is {@code double}
     *
     * @param klass      parameter
     * @param methodName parameter
     * @return return value
     */
    Method getJavaMethod(Class<?> klass, String methodName)
    ;

}

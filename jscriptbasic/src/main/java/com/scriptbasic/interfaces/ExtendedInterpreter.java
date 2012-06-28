package com.scriptbasic.interfaces;

import java.lang.reflect.Method;
import java.util.Map;

/**
 * The extended interpreter is the interface that extends the functions of the
 * Interpreter with the methods that are needed by the command implementations
 * but are not needed by the program that embeds the interpreter.
 * 
 * @author Peter Verhas
 * 
 */
public interface ExtendedInterpreter extends Interpreter {
    /**
     * Get the actual value of the program counter.
     * 
     * @return the actual command pointer
     */
    Integer getProgramCounter();

    /**
     * 
     * @return the program that the interpreter is executing.
     */
    BuildableProgram getProgram();

    /**
     * Tell the interpreter that the next command to call is not the one that
     * follows the actual command but rather the one specified by the argument.
     * 
     * @param nextCommand
     *            is the next command to execute after the current command
     * 
     */
    void setNextCommand(Command nextCommand);

    /**
     * Get the variables of the program.
     * 
     * @return the variables.
     */
    VariableMap getVariables();

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
     * 
     * <pre>
     * use Math from java.lang as m
     * </pre>
     * 
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
     * 
     * <pre>
     * use class from package as basicClassReference
     * </pre>
     * 
     * for example
     * 
     * <pre>
     * use Math from java.lang as m
     * </pre>
     * 
     * when the method {@code sin} is used, foe example
     * 
     * <pre>
     * a = m.sin(1.0)
     * </pre>
     * 
     * the BASIC interpreter has to find the method
     * {@code java.lang.Math.sin(Double x)}. The problem is that the method does
     * not exist because the argument is not {@code Double} but rather
     * {@code double}.
     * <p>
     * To help with this situation the BASIC program should declare the Java
     * signature of the method using the BASIC command METHOD. For example:
     * 
     * <pre>
     * method sin from java.lang.Math is (double) use as sinus
     * </pre>
     * 
     * (Note that the part {@code use as ...} is optional.)
     * <p>
     * After this command is executed the interpreter will use the defined
     * signature to locate the method. You can write in the BASIC program
     * 
     * <pre>
     * a = m.sinus(1.0)
     * </pre>
     * 
     * {@code registerJavaMethod()} registers the basic function alias, class,
     * java method name and the argument types so that later call to {@see
     * #getJavaMethod(Class, String)} can find the appropriate method.
     * 
     * @param klass
     * @param methodName
     * @param argumentTypes
     */
    void registerJavaMethod(String alias, Class<?> klass, String methodName,
            Class<?>[] argumentTypes);

    /**
     * Get the method named from the klass based on the declaration given in a
     * previously executed {@code METHOD} basic command. The basic command
     * METHOD has the form (example follows):
     * 
     * <pre>
     * method sin from java.lang.Math is (double)
     * </pre>
     * 
     * that defines that the
     * 
     * @param klass
     * @param mehodName
     * @return
     * @throws ExecutionException 
     */
    Method getJavaMethod(Class<?> klass, String methodName) throws ExecutionException;
}

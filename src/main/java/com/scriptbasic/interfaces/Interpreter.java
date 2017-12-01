package com.scriptbasic.interfaces;

import java.io.Reader;
import java.io.Writer;

/**
 * An interpreter instance executes a program.
 *
 * @author Peter Verhas
 */
public interface Interpreter {

    void registerHook(InterpreterHook hook);

    /**
     * Set the program to execute.
     *
     * @param buildableProgram
     */
    void setProgram(BuildableProgram buildableProgram);

    /**
     * Execute the program.
     */
    void execute() throws ExecutionException;

    /**
     * Set the value of the global variable.
     *
     * @param name  the name of the global variable
     * @param value the value to be set
     */
    void setVariable(String name, Object value) throws ExecutionException;

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
    Object getVariable(String name) throws ExecutionException;

    /**
     * Call a function defined by the program passing the objects as arguments.
     *
     * @param functionName the name of the function in the program code
     * @param arguments    the arguments to the function
     * @return the returned object, or {@code null} if the function does not
     * return value
     */
    Object call(String functionName, Object[] arguments) throws ExecutionException;

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
}

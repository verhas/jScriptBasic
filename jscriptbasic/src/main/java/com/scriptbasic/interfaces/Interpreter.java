package com.scriptbasic.interfaces;

/**
 * An interpreter instance executes a program.
 * 
 * @author Peter Verhas
 * 
 */
public interface Interpreter {
    /**
     * Set the program to execute.
     * 
     * @param buildableProgram
     */
    void setProgram(BuildableProgram buildableProgram);

    /**
     * Execute the program.
     */
    void execute();

    /**
     * Set the value of the global variable.
     * 
     * @param name
     *            the name of the global variable
     * @param value
     *            the value to be set
     */
    void setVariable(String name, Object value);

    /**
     * Get the value of a global variable.
     * 
     * @param name
     *            the name of the variable
     * @return the value of the variable
     */
    Object getVariable(String name);

    /**
     * Call a function defined by the program passing the objects as arguments.
     * 
     * @param functionName
     *            the name of the function in the program code
     * @param arguments
     *            the arguments to the function
     * @return the returned object, or {@code null} if the function does not
     *         return value
     */
    Object call(String functionName, Object[] arguments);
}

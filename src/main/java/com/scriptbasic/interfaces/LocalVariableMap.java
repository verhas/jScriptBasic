/**
 * 
 */
package com.scriptbasic.interfaces;

/**
 * A variable map structure that can manage many maps in a stack structure. Any
 * call to methods inherited from {@link VariableMap} works on the map at the top
 * of the stack.
 * 
 * @author Peter Verhas
 * date June 22, 2012
 * 
 */
public interface LocalVariableMap extends VariableMap {
    /**
     * Push the actual Map onto the stack of maps and create a new empty stack.
     * This is what a program has to do when a function is called. The local
     * variables (if any) are pushed on the stack and a new stack frame is
     * opened.
     */
    void newFrame();

    /**
     * Drop the current frame and pop off the map from the top of the stack.
     * This is what programs have to do when a RETURN is executed from a
     * function.
     */
    void dropFrame();

    /**
     * Register a variable as a global variable. This is needed when an
     * implementing class manages the global as well as the local variables.
     * <p>
     * Interpreters handle variables differently. When a variable is not defined
     * using some keyword as 'LOCAL' or 'GLOBAL' then the interpreter may treat
     * the variable default local, may treat default global or may raise error.
     * <p>
     * This method can be used to declare that a variable is used in the local
     * environment referring to the global variable.
     * 
     * @param variableName
     * @throws ExecutionException
     */
    void registerGlobalVariable(String variableName) throws ExecutionException;

    /**
     * Define the variable as a local variable in a local environment. For more
     * information see the documentation of the method {@link
     * #registerGlobalVariable(String)}
     * 
     * @param variableName
     * @throws ExecutionException
     */
    void registerLocalVariable(String variableName) throws ExecutionException;
}

/**
 *
 */
package com.scriptbasic.interfaces;
/**
 * Manage local or global variables. One Map manages the global variables in a
 * BASIC program. There is a map for each local level of function calls storing
 * the local variables. This interface is also implemented by the
 * MixedBasicVariableMap that manages the global and the local variables
 * transparently.
 * <p>
 * The methods may throw {@see ExecutionException}. The interface provides this
 * possibility to implement features like denying case sensitive reuse of
 * variable names. For example jScriptBasic does not allow you to use the
 * variable {@code Apple} is ever you already used {@code apple}. In other basic
 * implementations the casing is insensitive and the two variable names are the
 * same. Other languages treat {@code Apple} and {@code apple} as two different
 * variables. jScriptBasic believes that both practices are bad.
 *
 * @author Peter Verhas
 * date June 22, 2012
 *
 */
public interface VariableMap {
    /**
     * Get the value of a variable.
     *
     * @param variableName
     * @return the right value the variable has or {@code null} if the variable
     *         is not defined or does not exists.
     */
    RightValue getVariableValue(final String variableName)
            throws ExecutionException;
    /**
     * Checks that a variable exists in this map.
     *
     * @param variableName
     * @return {@code true} if the variable exists, even if the variable has
     *         undefined value. {@code false} if the variable does not exist in
     *         this map.
     */
    Boolean variableExists(final String variableName) throws ExecutionException;
    /**
     * Checks that a variable exists and has a defined value in this map.
     * Undefined value is represented by {@code null}.
     *
     * @param variableName
     * @return {@code true} if the variable exists and has a defined value.
     */
    Boolean variableDefined(final String variableName)
            throws ExecutionException;
    /**
     * Set the value of the variable. If the variable did not exist then the
     * variable is automatically created. If the variable had undefined value
     * then the new value will be the one defined by the argument.
     * <p>
     * You can set a variable to undefined passing {@code null} to this method
     * as {@code rightValue}.
     *
     * @param variableName
     * @param rightValue
     *            the new value of the variable
     */
    void setVariable(final String variableName, RightValue rightValue)
            throws ExecutionException;
}
package com.scriptbasic.memory;

import com.scriptbasic.interfaces.BasicRuntimeException;
import com.scriptbasic.interfaces.ExecutionException;
import com.scriptbasic.interfaces.RightValue;
import com.scriptbasic.interfaces.VariableMap;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Handle a variable Map.
 *
 * @author Peter Verhas
 * date June 22, 2012
 */
class BasicVariableMap implements VariableMap {

    private final Map<String, RightValue> variableMap = new HashMap<>();
    private final Set<String> variableNameSet = new HashSet<>();
    private boolean variableNamesAreCaseSensitive = false;
    private boolean variableNamesAreStrictCased = false;
    BasicVariableMap() {
    }

    BasicVariableMap(BasicVariableMap that) {
        this.variableNamesAreCaseSensitive = that.variableNamesAreCaseSensitive;
        this.variableNamesAreStrictCased = that.variableNamesAreStrictCased;
    }

    @Override
    public Set<String> getVariableNameSet() {
        return variableNameSet;
    }

    /**
     * Tell the object that the variable names are handled in a case sensitive
     * way. It means that the variables 'vaRIable' and 'variABLE' are two
     * different variables and can live in the global or the actual local
     * variable name space along with each other in peace.
     * <p>
     * It is not recommended to use this mode, however most modern programming
     * languages do this.
     */
    public void setCaseSensitive() {
        variableNamesAreCaseSensitive = true;
        variableNamesAreStrictCased = false;
    }

    /**
     * Tell the object that the variable names has to be cased only one way. It
     * means that there can not be a variable named "vaRIable' and 'variABLE' at
     * the same time.
     * <p>
     * It is the recommended behavior, however BASIC implementations rarely
     * exhibit such variable name handling this is not the default.
     */
    public void setCaseFreak() {
        variableNamesAreCaseSensitive = false;
        variableNamesAreStrictCased = true;
    }

    /**
     * Tell the object that the variable handling is case insensitive. It means
     * that the variables 'vaRIable' and 'variABLE' denote the same variable.
     * <p>
     * It is not recommended to use this mode, however most BASIC implementation
     * do this. This is the default behavior.
     */
    public void setCaseIgnorant() {
        variableNamesAreCaseSensitive = false;
        variableNamesAreStrictCased = false;
    }

    /**
     * Converts the name of a variable in a way that if the name 'a' and name
     * 'b' results the same string converted then 'a' and 'b' are treated from
     * some aspects as the same variable name. The generic approach is simply
     * upper casing.
     *
     * @param name
     * @return
     */
    protected String converted(String name) {
        return variableNamesAreCaseSensitive ? name : name.toUpperCase();
    }

    /**
     * Asserts that the variable is used with the correct casing. When a
     * variable is used the first time it is accepted by the interpreter as it
     * is. The next time the variable has to be used the same way, thus if the
     * first occurrence is vaRIable then the second can not be VArIabLe or any
     * other variation of casing.
     * <p>
     * We accept that a variable has correct casing when the casing is the one
     * we remember or if we do not remember the variable at all. In this latter
     * case the method does not insert the variable with the actual casing into
     * the registry hash because the invocation of the method does not guarantee
     * that the variable will be used in this format. It still may happen that
     * other checks stop the interpreter registering the variable and therefore
     * using it the way it is here.
     *
     * @param variableName
     * @throws ExecutionException
     */
    void assertCorrectCasing(String variableName) throws ExecutionException {
        if (variableNamesAreStrictCased) {
            String convertedName = converted(variableName);
            if (variableMap.containsKey(convertedName)
                    && !variableNameSet.contains(variableName)) {
                throw new BasicRuntimeException("Variable '" + variableName
                        + "' has different casing.");
            }
        }
    }

    /**
     * Register the variable with the given casing. (with the given form related
     * to upper and lower case letters)
     *
     * @param name
     */
    void registerVariableCasing(String name) {
        variableNameSet.add(name);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.scriptbasic.interfaces.VariableMap#getVariableValue(java.lang.String)
     */
    @Override
    public RightValue getVariableValue(String variableName)
            throws ExecutionException {
        return variableExists(variableName) ? variableMap
                .get(converted(variableName)) : null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.scriptbasic.interfaces.VariableMap#variableExists(java.lang.String)
     */
    @Override
    public Boolean variableExists(String variableName)
            throws ExecutionException {
        assertCorrectCasing(variableName);
        return variableMap.containsKey(converted(variableName));
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.scriptbasic.interfaces.VariableMap#variableDefined(java.lang.String)
     */
    @Override
    public Boolean variableDefined(String variableName)
            throws ExecutionException {
        return variableExists(variableName)
                && getVariableValue(variableName) != null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.scriptbasic.interfaces.VariableMap#setVariable(java.lang.String,
     * com.scriptbasic.interfaces.RightValue)
     */
    @Override
    public void setVariable(String variableName, RightValue rightValue)
            throws ExecutionException {
        assertCorrectCasing(variableName);
        registerVariableCasing(variableName);
        variableMap.put(converted(variableName), rightValue);
    }

}

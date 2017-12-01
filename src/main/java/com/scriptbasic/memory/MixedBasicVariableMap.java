package com.scriptbasic.memory;

import com.scriptbasic.interfaces.*;

/**
 * Handle the global and the local variable maps. If a variable exists, defined
 * in the local variable map, then that variable is used, otherwise the global
 * variable map is used.
 *
 * @author Peter Verhas
 * date June 22, 2012
 */
public class MixedBasicVariableMap extends BasicLocalVariableMap implements
        HierarchicalVariableMap {

    private final BasicVariableMap globalVariableMap = new BasicVariableMap();
    private final BasicVariableRegistry globalVariableRegistry = new BasicVariableRegistry(
            globalVariableMap);
    private boolean variablesCreatedByDefault = true;
    private boolean defaultVariableScopeIsGlobal = false;

    @Override
    public VariableMap getGlobalMap() {
        return globalVariableMap;
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * com.scriptbasic.interfaces.VariableMap#getVariableValue(java.lang.String)
     */
    @Override
    public RightValue getVariableValue(final String variableName)
            throws ExecutionException {
        return super.variableExists(variableName) ? super
                .getVariableValue(variableName) : globalVariableMap
                .getVariableValue(variableName);
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * com.scriptbasic.interfaces.VariableMap#variableExists(java.lang.String)
     */
    @Override
    public Boolean variableExists(final String variableName)
            throws ExecutionException {
        return super.variableExists(variableName)
                || globalVariableMap.variableExists(variableName);
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * com.scriptbasic.interfaces.VariableMap#variableDefined(java.lang.String)
     */
    @Override
    public Boolean variableDefined(final String variableName)
            throws ExecutionException {
        return super.variableDefined(variableName)
                || globalVariableMap.variableDefined(variableName);
    }

    /**
     * Inform the object that variables should not be created on the fly, but
     * first a variable has to be registered and can only be used afterwards.
     */
    public void requireVariableDeclaration() {
        variablesCreatedByDefault = false;
    }

    /**
     * Inform the object that variables have to be created on the fly without
     * previous proper registration. This is the usual BASIC way that BASIC
     * programmers got used to, though this is more dangerous.
     * <p>
     * Nevertheless this is the default behavior.
     */
    public void createVariablesOnTheFly() {
        variablesCreatedByDefault = true;
    }

    /**
     * Inform the object that when a variable is created on the fly without
     * prior declaration that would have specified if the variable is local or
     * global, then the variable has to be local by default.
     * <p>
     * This is the safer solution and this is the default, since there is no
     * previous BASIC style in this aspect.
     */
    public void defaultVariableScopeShallBeLocal() {
        defaultVariableScopeIsGlobal = false;
    }

    /**
     * Inform the object that when a variable is created on the fly without
     * prior declaration that would have specified if the variable is local or
     * global, then the variable has to be global by default.
     * <p>
     * This is generally dangerous, however it is up to the programmer embedding
     * the interpreter to his/her application.
     */
    public void defaultVariableScopeShallBeGlobal() {
        defaultVariableScopeIsGlobal = true;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.scriptbasic.interfaces.LocalVariableMap#registerGlobalVariable
     */
    @Override
    public void registerGlobalVariable(final String variableName)
            throws ExecutionException {
        super.registerGlobalVariable(variableName);
        globalVariableRegistry.registerGlobal(variableName);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.scriptbasic.interfaces.VariableMap#setVariable(java.lang.String,
     * com.scriptbasic.interfaces.RightValue)
     */
    @Override
    public void setVariable(final String variableName,
                            final RightValue rightValue) throws ExecutionException {
        if (super.isLocal(variableName)) {
            super.setVariable(variableName, rightValue);
        } else if (super.isGlobal(variableName)) {
            globalVariableMap.setVariable(variableName, rightValue);
        } else {
            if (variablesCreatedByDefault) {
                if (defaultVariableScopeIsGlobal || currentScopeIsGlobal()) {
                    globalVariableMap.setVariable(variableName, rightValue);
                } else {
                    super.setVariable(variableName, rightValue);
                }
            } else {
                throw new BasicRuntimeException("Variable '" + variableName
                        + "' was not properly declared");
            }
        }

    }

    @Override
    public void setCaseSensitive() {
        super.setCaseSensitive();
        globalVariableMap.setCaseSensitive();
    }

    @Override
    public void setCaseFreak() {
        super.setCaseFreak();
        globalVariableMap.setCaseFreak();
    }

    @Override
    public void setCaseIgnorant() {
        super.setCaseIgnorant();
        globalVariableMap.setCaseIgnorant();
    }
}

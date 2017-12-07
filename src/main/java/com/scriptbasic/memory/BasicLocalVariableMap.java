package com.scriptbasic.memory;

import com.scriptbasic.api.ScriptBasicException;
import com.scriptbasic.interfaces.LocalVariableMap;
import com.scriptbasic.interfaces.RightValue;

import java.util.Stack;

/**
 * Handle the local variable in a hierarchical stack of Maps
 *
 * @author Peter Verhas
 * date June 22, 2012
 */
public class BasicLocalVariableMap extends BasicVariableMap implements
        LocalVariableMap {

    private BasicVariableMap actualMap = null;
    private BasicVariableRegistry actualRegistry = null;
    private Stack<BasicVariableMap> localMapStack = new Stack<>();
    private Stack<BasicVariableRegistry> localRegistryStack = new Stack<>();

    @Override
    public RightValue getVariableValue(final String variableName)
            throws ScriptBasicException {
        return actualMap == null ? null : actualMap
                .getVariableValue(variableName);
    }

    @Override
    public Boolean variableExists(final String variableName)
            throws ScriptBasicException {
        return actualMap != null && actualMap.variableExists(variableName);
    }

    @Override
    public Boolean variableDefined(final String variableName)
            throws ScriptBasicException {
        return actualMap != null && actualMap.variableDefined(variableName);
    }

    @Override
    public void setVariable(final String variableName, final RightValue rightValue)
            throws ScriptBasicException {
        if (actualMap == null) {
            throw new IllegalArgumentException("setting local variable '"
                    + variableName + "' in non local scope");
        }
        actualMap.setVariable(variableName, rightValue);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.scriptbasic.interfaces.LocalVariableMap#newFrame()
     */
    @Override
    public void newFrame() {
        localMapStack.push(actualMap);
        localRegistryStack.push(actualRegistry);
        actualMap = new BasicVariableMap(this);
        actualRegistry = new BasicVariableRegistry(actualMap);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.scriptbasic.interfaces.LocalVariableMap#dropFrame()
     */
    @Override
    public void dropFrame() {
        actualMap = localMapStack.pop();
        actualRegistry = localRegistryStack.pop();
    }

    /**
     * Returns true if the current scope is global, there no no any local stack
     * frame open.
     *
     * @return
     */
    public boolean currentScopeIsGlobal() {
        return localMapStack.isEmpty();
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.scriptbasic.interfaces.LocalVariableMap#registerGlobalVariable
     */
    @Override
    public void registerGlobalVariable(final String variableName)
            throws ScriptBasicException {
        if (actualRegistry != null) {
            actualRegistry.registerGlobal(variableName);
        }
    }

    @Override
    public void registerLocalVariable(final String variableName)
            throws ScriptBasicException {
        if (actualRegistry != null) {
            actualRegistry.registerLocal(variableName);
        } else {
            throw new IllegalArgumentException("registering local variable +'"
                    + variableName + "' in non local scope");
        }
    }

    public boolean isGlobal(final String variableName) throws ScriptBasicException {
        return actualRegistry != null && actualRegistry.isGlobal(variableName);
    }

    public boolean isLocal(final String variableName) throws ScriptBasicException {
        return actualRegistry != null && actualRegistry.isLocal(variableName);
    }

}

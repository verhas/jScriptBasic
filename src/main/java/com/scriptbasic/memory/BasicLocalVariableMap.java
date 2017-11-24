package com.scriptbasic.memory;

import java.util.Stack;

import com.scriptbasic.interfaces.ExecutionException;
import com.scriptbasic.interfaces.LocalVariableMap;
import com.scriptbasic.interfaces.RightValue;

/**
 * Handle the local variable in a hierarchical stack of Maps
 * 
 * @author Peter Verhas
 * date June 22, 2012
 * 
 */
public class BasicLocalVariableMap extends BasicVariableMap implements
        LocalVariableMap {

    private BasicVariableMap actualMap = null;
    private BasicVariableRegistry actualRegistry = null;

    @Override
    public RightValue getVariableValue(String variableName)
            throws ExecutionException {
        return actualMap == null ? null : actualMap
                .getVariableValue(variableName);
    }

    @Override
    public Boolean variableExists(String variableName)
            throws ExecutionException {
        return actualMap != null && actualMap.variableExists(variableName);
    }

    @Override
    public Boolean variableDefined(String variableName)
            throws ExecutionException {
        return actualMap != null && actualMap.variableDefined(variableName);
    }

    @Override
    public void setVariable(String variableName, RightValue rightValue)
            throws ExecutionException {
        if (actualMap == null) {
            throw new IllegalArgumentException("setting local variable '"
                    + variableName + "' in non local scope");
        }
        actualMap.setVariable(variableName, rightValue);
    }

    private Stack<BasicVariableMap> localMapStack = new Stack<>();
    private Stack<BasicVariableRegistry> localRegistryStack = new Stack<>();

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
    public void registerGlobalVariable(String variableName)
            throws ExecutionException {
        if (actualRegistry != null) {
            actualRegistry.registerGlobal(variableName);
        }
    }

    @Override
    public void registerLocalVariable(String variableName)
            throws ExecutionException {
        if (actualRegistry != null) {
            actualRegistry.registerLocal(variableName);
        } else {
            throw new IllegalArgumentException("registering local variable +'"
                    + variableName + "' in non local scope");
        }
    }

    public boolean isGlobal(String variableName) throws ExecutionException {
        return actualRegistry != null && actualRegistry.isGlobal(variableName);
    }

    public boolean isLocal(String variableName) throws ExecutionException {
        return actualRegistry != null && actualRegistry.isLocal(variableName);
    }

}

package com.scriptbasic.executors.leftvalues;

import java.util.LinkedList;
import java.util.List;

import com.scriptbasic.interfaces.ExecutionException;
import com.scriptbasic.interfaces.RightValue;
import com.scriptbasic.interfaces.VariableMap;

/**
 * 
 * @author Peter Verhas
 * @date June 13, 2012
 * 
 */
public class BasicLeftValue extends AbstractLeftValue {

    /**
     * The identifier that is the name of the local or global variable.
     */
    private String identifier;

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(final String identifier) {
        this.identifier = identifier;
    }

    final List<LeftValueModifier> modifiers = new LinkedList<LeftValueModifier>();

    public List<LeftValueModifier> getModifiers() {
        return modifiers;
    }

    public void addModifier(LeftValueModifier modifier) {
        modifiers.add(modifier);
    }

    @Override
    public void setValue(final RightValue rightValue, VariableMap variableMap) throws ExecutionException {
        if (modifiers == null || modifiers.isEmpty()) {
            variableMap.setVariable(getIdentifier(), rightValue);
        } else {
            throw new RuntimeException("not implemened yet");
        }
    }

}

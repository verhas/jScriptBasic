package com.scriptbasic.executors.commands;
import com.scriptbasic.executors.leftvalues.BasicLeftValue;
import com.scriptbasic.interfaces.ExecutionException;
import com.scriptbasic.interfaces.ExtendedInterpreter;
import com.scriptbasic.interfaces.LeftValue;
public class CommandLocal extends AbstractCommandLeftValueListed {
    @Override
    public void execute(final ExtendedInterpreter interpreter)
            throws ExecutionException {
        for (LeftValue variable : getLeftValueList()) {
            // TODO instance check and that the modifier list is null
            String variableName = ((BasicLeftValue) variable).getIdentifier();
            interpreter.getVariables().registerLocalVariable(variableName);
        }
    }
}
package com.scriptbasic.executors.commands;

import com.scriptbasic.api.ScriptBasicException;
import com.scriptbasic.executors.leftvalues.BasicLeftValue;
import com.scriptbasic.spi.Interpreter;
import com.scriptbasic.spi.LeftValue;

public class CommandGlobal extends AbstractCommandLeftValueListed {

    @Override
    public void execute(final Interpreter interpreter)
            throws ScriptBasicException {
        for (final LeftValue variable : getLeftValueList()) {
            // TODO instance check and that the modifier list is null
            final var variableName = ((BasicLeftValue) variable).getIdentifier();
            interpreter.getVariables().registerGlobalVariable(variableName);
        }
    }
}

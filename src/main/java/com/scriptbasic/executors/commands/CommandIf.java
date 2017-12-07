package com.scriptbasic.executors.commands;

import com.scriptbasic.api.ScriptBasicException;
import com.scriptbasic.interfaces.Interpreter;

public class CommandIf extends AbstractCommandIfKind {

    @Override
    public void execute(final Interpreter interpreter)
            throws ScriptBasicException {
        if (!theConditionIsTrue(interpreter)) {
            indicateConditionalJump(interpreter);
            interpreter.setNextCommand(getNext());
        }
    }

}

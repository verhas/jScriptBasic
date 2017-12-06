package com.scriptbasic.executors.commands;

import com.scriptbasic.interfaces.ExecutionException;
import com.scriptbasic.interfaces.Interpreter;

public class CommandIf extends AbstractCommandIfKind {

    @Override
    public void execute(final Interpreter interpreter)
            throws ExecutionException {
        if (!theConditionIsTrue(interpreter)) {
            indicateConditionalJump(interpreter);
            interpreter.setNextCommand(getNext());
        }
    }

}

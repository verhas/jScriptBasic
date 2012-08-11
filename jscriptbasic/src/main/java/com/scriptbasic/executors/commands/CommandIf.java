package com.scriptbasic.executors.commands;
import com.scriptbasic.interfaces.ExecutionException;
import com.scriptbasic.interfaces.ExtendedInterpreter;
public class CommandIf extends AbstractCommandIfKind {
    @Override
    public void execute(final ExtendedInterpreter interpreter)
            throws ExecutionException {
        if (!theConditionIsTrue(interpreter)) {
            indicateConditionalJump(interpreter);
            interpreter.setNextCommand(getNext());
        }
    }
}
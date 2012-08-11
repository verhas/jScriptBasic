package com.scriptbasic.executors.commands;
import com.scriptbasic.interfaces.ExecutionException;
import com.scriptbasic.interfaces.ExtendedInterpreter;
public class CommandElseIf extends AbstractCommandIfKind {
    @Override
    public void execute(final ExtendedInterpreter interpreter)
            throws ExecutionException {
        if (itWasConditionalJump(interpreter)) {
            if (conditionWasNotDoneYet(interpreter)
                    && theConditionIsTrue(interpreter)) {
                jumpDone(interpreter);
            } else {
                indicateConditionalJump(interpreter);
                interpreter.setNextCommand(getNext());
            }
        } else {
            indicateConditionDone(interpreter);
            indicateConditionalJump(interpreter);
            interpreter.setNextCommand(getNext());
        }
    }
}
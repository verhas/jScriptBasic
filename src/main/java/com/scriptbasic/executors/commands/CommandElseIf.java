package com.scriptbasic.executors.commands;

import com.scriptbasic.api.ScriptBasicException;
import com.scriptbasic.spi.Interpreter;

public class CommandElseIf extends AbstractCommandIfKind {

    @Override
    public void execute(final Interpreter interpreter)
            throws ScriptBasicException {

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

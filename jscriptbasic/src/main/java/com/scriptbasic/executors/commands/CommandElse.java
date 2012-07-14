package com.scriptbasic.executors.commands;

import com.scriptbasic.interfaces.ExtendedInterpreter;

public class CommandElse extends AbstractCommandIfElseKind {

    @Override
    public void execute(final ExtendedInterpreter interpreter) {
        if (itWasConditionalJump(interpreter)
                && conditionWasNotDoneYet(interpreter)) {
            jumpDone(interpreter);
        } else {
            indicateConditionalJump(interpreter);
            interpreter.setNextCommand(getNext());
        }
    }

}

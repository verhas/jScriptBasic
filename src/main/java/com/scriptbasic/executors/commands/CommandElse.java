package com.scriptbasic.executors.commands;

import com.scriptbasic.spi.Interpreter;

public class CommandElse extends AbstractCommandIfElseKind {

    @Override
    public void execute(final Interpreter interpreter) {
        if (itWasConditionalJump(interpreter)
                && conditionWasNotDoneYet(interpreter)) {
            jumpDone(interpreter);
        } else {
            indicateConditionalJump(interpreter);
            interpreter.setNextCommand(getNext());
        }
    }

}

package com.scriptbasic.executors.commands;

import com.scriptbasic.interfaces.ExtendedInterpreter;

public class CommandEndIf extends AbstractCommandIfElseKind {

    @Override
    public void execute(final ExtendedInterpreter interpreter) {
        jumpDone(interpreter);
    }
}

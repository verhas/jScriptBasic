package com.scriptbasic.executors.commands;

import com.scriptbasic.interfaces.Interpreter;

public class CommandEndIf extends AbstractCommandIfElseKind {

    @Override
    public void execute(final Interpreter interpreter) {
        jumpDone(interpreter);
    }
}

package com.scriptbasic.executors.commands;

import com.scriptbasic.interfaces.Interpreter;

public class CommandEndSub extends AbstractCommand {

    @Override
    public void execute(final Interpreter interpreter) {
        interpreter.setNextCommand(null);
    }

}

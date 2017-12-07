package com.scriptbasic.executors.commands;

import com.scriptbasic.spi.Interpreter;

public class CommandEndSub extends AbstractCommand {

    @Override
    public void execute(final Interpreter interpreter) {
        interpreter.setNextCommand(null);
    }

}

package com.scriptbasic.executors.commands;

import com.scriptbasic.spi.Interpreter;

public class CommandWend extends AbstractCommand {
    private CommandWhile commandWhile;

    public CommandWhile getCommandWhile() {
        return commandWhile;
    }

    public void setCommandWhile(final CommandWhile commandWhile) {
        this.commandWhile = commandWhile;
    }

    @Override
    public void execute(final Interpreter interpreter) {
        interpreter.setNextCommand(commandWhile);
    }

}

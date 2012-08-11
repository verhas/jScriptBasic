package com.scriptbasic.executors.commands;
import com.scriptbasic.interfaces.ExtendedInterpreter;
public class CommandWend extends AbstractCommand {
    private CommandWhile commandWhile;
    public CommandWhile getCommandWhile() {
        return commandWhile;
    }
    public void setCommandWhile(CommandWhile commandWhile) {
        this.commandWhile = commandWhile;
    }
    @Override
    public void execute(final ExtendedInterpreter interpreter) {
        interpreter.setNextCommand(commandWhile);
    }
}
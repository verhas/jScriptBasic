package com.scriptbasic.syntax;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.scriptbasic.executors.commands.AbstractCommand;
import com.scriptbasic.interfaces.BuildableProgram;
import com.scriptbasic.interfaces.Command;
import com.scriptbasic.interfaces.Factory;

public final class BasicProgram implements BuildableProgram {
    private Factory factory;

    public Factory getFactory() {
        return factory;
    }

    @Override
    public void setFactory(final Factory factory) {
        this.factory = factory;
    }

    private BasicProgram() {
    }

    private final List<Command> commands = new ArrayList<Command>();
    private AbstractCommand lastCommand = null;

    public void addCommand(final Command command) {
        if (lastCommand != null) {
            lastCommand.setNextCommand(command);
        }
        lastCommand = (AbstractCommand) command;
        this.commands.add(command);
    }

    @Override
    public Command getStartCommand() {
        Command startCommand;
        if (commands.isEmpty()) {
            startCommand = null;
        } else {
            startCommand = commands.get(0);
        }
        return startCommand;
    }

    @Override
    public Collection<Command> getCommands() {
        return commands;
    }

}

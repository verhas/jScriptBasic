package com.scriptbasic.syntax;

import java.util.ArrayList;
import java.util.Collection;

import com.scriptbasic.interfaces.BuildableProgram;
import com.scriptbasic.interfaces.Command;
import com.scriptbasic.interfaces.Factory;

public final class BasicProgram implements BuildableProgram {
    private Factory factory;

    public Factory getFactory() {
        return factory;
    }

    @Override
    public void setFactory(Factory factory) {
        this.factory = factory;
    }

    private BasicProgram() {
    }

    private final ArrayList<Command> commands = new ArrayList<Command>();

    public void addCommand(final Command command) {
        this.commands.add(command);
    }

    @Override
    public Command getStartCommand() {
        return null;
    }

    @Override
    public Collection<Command> getCommands() {
        return commands;
    }

    @Override
    public Command getCommand(final Integer programCounter) {

        return null;
    }

}

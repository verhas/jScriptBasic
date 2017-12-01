package com.scriptbasic.syntax;

import com.scriptbasic.executors.commands.AbstractCommand;
import com.scriptbasic.executors.commands.CommandSub;
import com.scriptbasic.interfaces.Command;

import java.util.*;

public final class BasicProgram extends AbstractBasicProgramPostprocessing {

    private final List<Command> commands = new ArrayList<>();
    private AbstractCommand lastCommand = null;
    private Map<String, CommandSub> subroutineMap = new HashMap<>();

    @Override
    public void reset() {
        commands.clear();
        lastCommand = null;
    }

    public void addCommand(final Command command) {
        if (lastCommand != null) {
            lastCommand.setNextCommand(command);
        }
        lastCommand = (AbstractCommand) command;
        this.commands.add(command);
    }

    protected Command getFirstCommand() {
        if (commands.isEmpty()) {
            return null;
        } else {
            return commands.get(0);
        }
    }

    @Override
    public Collection<Command> getCommands() {
        return commands;
    }

    @Override
    public Iterable<String> getNamedCommandNames() {
        return subroutineMap.keySet();
    }

    /**
     * @return the subroutineMap
     */
    protected Map<String, CommandSub> getSubroutineMap() {
        return subroutineMap;
    }

    @Override
    public Command getNamedCommand(String name) {
        return subroutineMap.get(name);
    }

}

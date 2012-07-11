package com.scriptbasic.executors.commands;

import com.scriptbasic.interfaces.ExtendedInterpreter;

public class CommandElse extends AbstractCommand implements IfOrElse {
    private CommandEndIf commandEndIf;

    public CommandEndIf getCommandEndIf() {
        return commandEndIf;
    }

    public void setCommandEndIf(CommandEndIf commandEndIf) {
        this.commandEndIf = commandEndIf;
    }

    @Override
    public void execute(final ExtendedInterpreter interpreter) {
        // TODO implement the command ELSE
    }

}

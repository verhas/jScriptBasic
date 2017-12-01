package com.scriptbasic.executors.commands;

import com.scriptbasic.interfaces.ExtendedInterpreter;

public class CommandUse extends AbstractCommand {

    private final Class<?> klass;
    private final String alias;

    public CommandUse(final Class<?> klass, final String alias) {
        this.klass = klass;
        this.alias = alias;
    }

    @Override
    public void execute(final ExtendedInterpreter interpreter) {
        interpreter.getUseMap().put(alias, klass);
    }

}

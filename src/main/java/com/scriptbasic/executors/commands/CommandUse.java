package com.scriptbasic.executors.commands;

import com.scriptbasic.api.ScriptBasicException;
import com.scriptbasic.spi.Interpreter;

public class CommandUse extends AbstractInsecureCommand {
    private final Class<?> klass;
    private final String alias;

    public CommandUse(final Class<?> klass, final String alias) {
        this.klass = klass;
        this.alias = alias;
    }

    @Override
    public void execute(final Interpreter interpreter) throws ScriptBasicException {
        assertInsecure(interpreter);
        interpreter.getUseMap().put(alias, klass);
    }

}

package com.scriptbasic.executors.commands;

import com.scriptbasic.interfaces.*;

public abstract class AbstractCommand implements Executor, Command,
        NestedStructure {

    private Command nextCommand;

    @Override
    public abstract void execute(Interpreter interpreter)
            throws ExecutionException;

    public void checkedExecute(final Interpreter interpreter)
            throws ExecutionException {
        execute(interpreter);
    }

    /**
     * Get the next command that has to be executed unless some condition alters
     * this, like in case of If, While and similar.
     *
     * @return
     */
    @Override
    public Command getNextCommand() {
        return nextCommand;
    }

    public void setNextCommand(final Command nextCommand) {
        this.nextCommand = nextCommand;
    }
}

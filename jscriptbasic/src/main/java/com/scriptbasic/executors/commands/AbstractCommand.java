package com.scriptbasic.executors.commands;

import com.scriptbasic.interfaces.BasicRuntimeException;
import com.scriptbasic.interfaces.Command;
import com.scriptbasic.interfaces.Configuration;
import com.scriptbasic.interfaces.ExecutionException;
import com.scriptbasic.interfaces.Executor;
import com.scriptbasic.interfaces.ExtendedInterpreter;
import com.scriptbasic.interfaces.NestedStructure;

public abstract class AbstractCommand implements Executor, Command,
        NestedStructure {

    @Override
    public abstract void execute(ExtendedInterpreter interpreter)
            throws ExecutionException;

    private boolean ignore(ExtendedInterpreter interpreter)
            throws ExecutionException {
        Configuration config = interpreter.getConfiguration();
        String key = "command." + this.getClass().getName();
        boolean returnValue = false;
        String permission = config.getConfigValue(key);
        if (permission != null) {
            switch (permission) {
            case "ignore":
                returnValue = true;
                break;
            case "fail":
            default:
                throw new BasicRuntimeException("Command can not execute "
                        + key);
            }
        }
        return returnValue;
    }

    public void checkedExecute(ExtendedInterpreter interpreter)
            throws ExecutionException {
        if (!ignore(interpreter)) {
            execute(interpreter);
        }
    }

    private Command nextCommand;

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

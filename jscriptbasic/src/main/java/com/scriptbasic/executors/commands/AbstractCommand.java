package com.scriptbasic.executors.commands;

import com.scriptbasic.interfaces.Command;
import com.scriptbasic.interfaces.Executor;
import com.scriptbasic.interfaces.ExtendedInterpreter;

public abstract class AbstractCommand implements Executor, Command {

    @Override
    public abstract void execute(ExtendedInterpreter interpreter);

}

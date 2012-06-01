package com.scriptbasic.executors.commands;

import com.scriptbasic.interfaces.Executor;
import com.scriptbasic.interfaces.ExtendedInterpreter;

public abstract class AbstractCommand implements Executor {

    @Override
    public abstract void execute(ExtendedInterpreter interpreter);

}

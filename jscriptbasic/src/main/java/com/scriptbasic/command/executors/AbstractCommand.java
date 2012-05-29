package com.scriptbasic.command.executors;

import com.scriptbasic.interfaces.Executor;
import com.scriptbasic.interfaces.ExtendedInterpreter;

public abstract class AbstractCommand implements Executor {

    @Override
    public abstract void execute(ExtendedInterpreter interpreter);

}

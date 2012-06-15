package com.scriptbasic.executors.commands;

import com.scriptbasic.interfaces.Command;
import com.scriptbasic.interfaces.Executor;
import com.scriptbasic.interfaces.ExtendedInterpreter;
import com.scriptbasic.interfaces.NestedStructure;

public abstract class AbstractCommand implements Executor, Command, NestedStructure {

    @Override
    public abstract void execute(ExtendedInterpreter interpreter);

}

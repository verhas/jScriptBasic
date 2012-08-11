package com.scriptbasic.executors.commands;
import com.scriptbasic.interfaces.ExtendedInterpreter;
public class CommandEndSub extends AbstractCommand {
    @Override
    public void execute(final ExtendedInterpreter interpreter) {
        interpreter.setNextCommand(null);
    }
}
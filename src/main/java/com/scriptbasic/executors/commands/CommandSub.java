package com.scriptbasic.executors.commands;

import com.scriptbasic.interfaces.ExtendedInterpreter;
import com.scriptbasic.interfaces.LeftValueList;

public class CommandSub extends AbstractCommand {
    private CommandEndSub commandEndSub;
    private LeftValueList arguments;
    private String subName;

    /**
     * @return the subName
     */
    public String getSubName() {
        return subName;
    }

    /**
     * @param subName the subName to set
     */
    public void setSubName(final String subName) {
        this.subName = subName;
    }

    /**
     * @return the commandEndSub
     */
    public CommandEndSub getCommandEndSub() {
        return commandEndSub;
    }

    /**
     * @param commandEndSub the commandEndSub to set
     */
    public void setCommandEndSub(final CommandEndSub commandEndSub) {
        this.commandEndSub = commandEndSub;
    }

    /**
     * @return the arguments
     */
    public LeftValueList getArguments() {
        return arguments;
    }

    /**
     * @param arguments the arguments to set
     */
    public void setArguments(final LeftValueList arguments) {
        this.arguments = arguments;
    }

    /**
     * Just jump over to the end sub and over that. Calling the function is
     * implemented in the expressions and in command CALL.
     * <p>
     * {@inheritDoc}
     */
    @Override
    public void execute(final ExtendedInterpreter interpreter) {
        interpreter.setNextCommand(commandEndSub.getNextCommand());
    }

}

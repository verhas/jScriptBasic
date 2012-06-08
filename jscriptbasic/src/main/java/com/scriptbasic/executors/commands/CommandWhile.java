package com.scriptbasic.executors.commands;

import com.scriptbasic.interfaces.Expression;
import com.scriptbasic.interfaces.ExtendedInterpreter;

public class CommandWhile extends AbstractCommand {
    private Integer wendProgramCounter;
    private Expression condition;

    public Expression getCondition() {
        return condition;
    }

    public void setCondition(Expression condition) {
        this.condition = condition;
    }

    public Integer getWendProgramCounter() {
        return this.wendProgramCounter;
    }

    public void setWendProgramCounter(final Integer wendProgramCounter) {
        this.wendProgramCounter = wendProgramCounter;
    }

    @Override
    public void execute(final ExtendedInterpreter interpreter) {
        // TODO Auto-generated method stub
    }

}

package com.scriptbasic.executors.commands;

import com.scriptbasic.interfaces.Expression;
import com.scriptbasic.interfaces.ExtendedInterpreter;

public class CommandIf extends AbstractCommand implements IfOrElse {
    private CommandElse elseNode;
    private CommandEndIf endIfNode;
    private Expression condition;

    public Expression getCondition() {
        return condition;
    }

    public void setCondition(Expression condition) {
        this.condition = condition;
    }

    public CommandElse getElseNode() {
        return elseNode;
    }

    public void setElseNode(CommandElse elseNode) {
        this.elseNode = elseNode;
    }

    public CommandEndIf getEndIfNode() {
        return endIfNode;
    }

    public void setEndIfNode(CommandEndIf endIfNode) {
        this.endIfNode = endIfNode;
    }

    @Override
    public void execute(final ExtendedInterpreter interpreter) {
        // TODO Auto-generated method stub
    }

}

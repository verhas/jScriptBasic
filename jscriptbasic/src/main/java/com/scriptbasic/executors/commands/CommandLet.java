package com.scriptbasic.executors.commands;

import com.scriptbasic.interfaces.Expression;
import com.scriptbasic.interfaces.ExtendedInterpreter;
import com.scriptbasic.interfaces.LeftValue;

public class CommandLet extends AbstractCommand implements IfOrElse {
    private LeftValue leftValue;
    private Expression expression;

    public LeftValue getLeftValue() {
        return leftValue;
    }

    public void setLeftValue(LeftValue leftValue) {
        this.leftValue = leftValue;
    }

    public Expression getExpression() {
        return expression;
    }

    public void setExpression(Expression expression) {
        this.expression = expression;
    }

    @Override
    public void execute(final ExtendedInterpreter interpreter) {
        // TODO Auto-generated method stub
    }

}

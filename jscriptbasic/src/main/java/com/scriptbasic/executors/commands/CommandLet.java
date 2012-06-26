package com.scriptbasic.executors.commands;

import com.scriptbasic.interfaces.BasicRuntimeException;
import com.scriptbasic.interfaces.ExecutionException;
import com.scriptbasic.interfaces.Expression;
import com.scriptbasic.interfaces.ExtendedInterpreter;
import com.scriptbasic.interfaces.LeftValue;

public class CommandLet extends AbstractCommand implements IfOrElse {
    private LeftValue leftValue;
    private Expression expression;

    public void setLeftValue(final LeftValue leftValue) {
        this.leftValue = leftValue;
    }

    public void setExpression(final Expression expression) {
        this.expression = expression;
    }

    @Override
    public void execute(final ExtendedInterpreter interpreter)
            throws ExecutionException {
        try {
            this.leftValue.setValue(this.expression.evaluate(interpreter),
                    interpreter);
        } catch (Exception e) {
            throw new BasicRuntimeException(e);
        }
    }

}

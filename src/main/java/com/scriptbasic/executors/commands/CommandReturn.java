package com.scriptbasic.executors.commands;

import com.scriptbasic.api.ScriptBasicException;
import com.scriptbasic.interfaces.Expression;
import com.scriptbasic.spi.Interpreter;

public class CommandReturn extends AbstractCommand {
    private Expression returnExpression;

    /**
     * @return the returnExpression
     */
    public Expression getReturnExpression() {
        return returnExpression;
    }

    /**
     * @param returnExpression the returnExpression to set
     */
    public void setReturnExpression(final Expression returnExpression) {
        this.returnExpression = returnExpression;
    }

    @Override
    public void execute(final Interpreter interpreter)
            throws ScriptBasicException {
        final var returnValue = returnExpression == null ? null
                : returnExpression.evaluate(interpreter);
        interpreter.setReturnValue(returnValue);
        interpreter.setNextCommand(null);
    }

}

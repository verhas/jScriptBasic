package com.scriptbasic.executors.commands;

import com.scriptbasic.interfaces.ExecutionException;
import com.scriptbasic.interfaces.Expression;
import com.scriptbasic.interfaces.ExtendedInterpreter;
import com.scriptbasic.interfaces.RightValue;

public class CommandReturn extends AbstractCommand {
    private Expression returnExpression;

    /**
     * @return the returnExpression
     */
    public Expression getReturnExpression() {
        return returnExpression;
    }

    /**
     * @param returnExpression
     *            the returnExpression to set
     */
    public void setReturnExpression(Expression returnExpression) {
        this.returnExpression = returnExpression;
    }

    @Override
    public void execute(final ExtendedInterpreter interpreter)
            throws ExecutionException {
        RightValue returnValue = returnExpression == null ? null
                : returnExpression.evaluate(interpreter);
        interpreter.setReturnValue(returnValue);
        interpreter.setNextCommand(null);
    }

}

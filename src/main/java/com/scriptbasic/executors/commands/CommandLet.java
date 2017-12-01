package com.scriptbasic.executors.commands;

import com.scriptbasic.interfaces.*;

public class CommandLet extends AbstractCommand {
    private LeftValue leftValue;
    private Expression expression;

    public void setLeftValue(final LeftValue leftValue) {
        this.leftValue = leftValue;
    }

    public void setExpression(final Expression expression) {
        this.expression = expression;
    }

    @Override
    public void execute(final ExtendedInterpreter interpreter) throws ExecutionException {
        try {
            final RightValue rv = expression.evaluate(interpreter);
            if (resultHasToBeStored()) {
                leftValue.setValue(rv, interpreter);
            }
        } catch (final Exception e) {
            throw new BasicRuntimeException(e);
        }
    }

    /**
     * When there is no left value it means that we evaluate the expression but we do not store
     * the result. It happens when a bare 'object.method()' is the command line and the result is
     * not stored in any BASIC variable. Such a line is compiled by the CALL analyzer.
     *
     * @return true if we can store the result in a left value
     */
    private boolean resultHasToBeStored() {
        return leftValue != null;
    }

}

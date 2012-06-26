package com.scriptbasic.executors.commands;

import com.scriptbasic.executors.rightvalues.AbstractPrimitiveRightValue;
import com.scriptbasic.interfaces.BasicRuntimeException;
import com.scriptbasic.interfaces.Command;
import com.scriptbasic.interfaces.ExecutionException;
import com.scriptbasic.interfaces.Expression;
import com.scriptbasic.interfaces.ExtendedInterpreter;
import com.scriptbasic.interfaces.RightValue;

public class CommandWhile extends AbstractCommand {
    private Command wendNode;

    public Command getWendNode() {
        return wendNode;
    }

    public void setWendNode(Command wendNode) {
        this.wendNode = wendNode;
    }

    private Expression condition;

    public Expression getCondition() {
        return condition;
    }

    public void setCondition(Expression condition) {
        this.condition = condition;
    }

    @SuppressWarnings("unchecked")
    @Override
    public void execute(final ExtendedInterpreter interpreter)
            throws ExecutionException {
        final RightValue conditionValue = getCondition().evaluate(interpreter);
        if (conditionValue instanceof AbstractPrimitiveRightValue<?>) {
            final AbstractPrimitiveRightValue<Object> condition = (AbstractPrimitiveRightValue<Object>) conditionValue;
            if (!condition.getBooleanValue()) {
                // jump after the WEND command
                interpreter.setNextCommand(getWendNode().getNextCommand());
            }
        } else {
            throw new BasicRuntimeException(
                    "While condition can not be evaluated to boolean");
        }
    }

}

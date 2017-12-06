package com.scriptbasic.executors.commands;

import com.scriptbasic.executors.rightvalues.BasicBooleanValue;
import com.scriptbasic.interfaces.ExecutionException;
import com.scriptbasic.interfaces.Expression;
import com.scriptbasic.interfaces.Interpreter;
import com.scriptbasic.interfaces.RightValue;

/**
 * @author Peter Verhas
 * date Jul 12, 2012
 */
public abstract class AbstractCommandIfKind extends AbstractCommandIfElseKind {

    private Expression condition;

    public Expression getCondition() {
        return condition;
    }

    public void setCondition(final Expression condition) {
        this.condition = condition;
    }

    protected Boolean theConditionIsTrue(final Interpreter interpreter)
            throws ExecutionException {
        final RightValue conditionRightValue = getCondition().evaluate(interpreter);
        return BasicBooleanValue.asBoolean(conditionRightValue);
    }
}

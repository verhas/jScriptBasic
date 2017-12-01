package com.scriptbasic.executors.commands;

import com.scriptbasic.executors.rightvalues.BasicBooleanValue;
import com.scriptbasic.interfaces.ExecutionException;
import com.scriptbasic.interfaces.Expression;
import com.scriptbasic.interfaces.ExtendedInterpreter;
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

    public void setCondition(Expression condition) {
        this.condition = condition;
    }

    protected Boolean theConditionIsTrue(ExtendedInterpreter interpreter)
            throws ExecutionException {
        RightValue conditionRightValue = getCondition().evaluate(interpreter);
        return BasicBooleanValue.asBoolean(conditionRightValue);
    }
}

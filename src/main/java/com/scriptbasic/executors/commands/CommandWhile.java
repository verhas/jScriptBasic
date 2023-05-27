package com.scriptbasic.executors.commands;

import com.scriptbasic.api.ScriptBasicException;
import com.scriptbasic.context.CompilerContext;
import com.scriptbasic.executors.rightvalues.AbstractPrimitiveRightValue;
import com.scriptbasic.executors.rightvalues.BasicBooleanValue;
import com.scriptbasic.interfaces.BasicRuntimeException;
import com.scriptbasic.interfaces.Expression;
import com.scriptbasic.spi.Command;
import com.scriptbasic.spi.Interpreter;

public class CommandWhile extends AbstractCommand {
    private Command wendNode;
    private Expression condition;

    public Command getWendNode() {
        return wendNode;
    }

    public void setWendNode(final Command wendNode) {
        this.wendNode = wendNode;
    }

    public Expression getCondition() {
        return condition;
    }

    public void setCondition(final Expression condition) {
        this.condition = condition;
    }

    private void jumpAfterTheWendCommand(final Interpreter interpreter) {
        interpreter.setNextCommand(getWendNode().getNextCommand());
    }

    @Override
    public String toJava(CompilerContext cc) {
        final var sb = new StringBuilder();
        sb.append("if(").append(getCondition().toJava(cc)).append("){\n");
        sb.append("  _pc++;\n");
        sb.append("  } else {\n");
        final var commandAfterWend = getWendNode().getNextCommand();
        if (commandAfterWend == null) {
            sb.append("return;\n");
        } else {
            sb.append("_pc=%d;\n".formatted(cc.nr.get(commandAfterWend)));
        }
        sb.append("}");
        return sb.toString();
    }

    @Override
    public void execute(final Interpreter interpreter)
            throws ScriptBasicException {
        final var conditionValue = getCondition().evaluate(interpreter);
        if (conditionValue instanceof AbstractPrimitiveRightValue<?>) {
            if (!BasicBooleanValue.asBoolean(conditionValue)) {
                jumpAfterTheWendCommand(interpreter);
            }
        } else {
            throw new BasicRuntimeException(
                    "While condition can not be evaluated to boolean");
        }
    }

}

package com.scriptbasic.executors.commands;

import java.io.IOException;
import java.io.Writer;

import com.scriptbasic.executors.rightvalues.BasicStringValue;
import com.scriptbasic.interfaces.BasicRuntimeException;
import com.scriptbasic.interfaces.ExecutionException;
import com.scriptbasic.interfaces.Expression;
import com.scriptbasic.interfaces.ExtendedInterpreter;
import com.scriptbasic.interfaces.RightValue;

public class CommandPrint extends AbstractCommandExpressionListed {

    @Override
    public void execute(final ExtendedInterpreter interpreter)
            throws ExecutionException {
        for (Expression expression : getExpressionList()) {
            RightValue rightValue = expression.evaluate(interpreter);
            try {
                Writer writer = interpreter.getWriter();
                if (writer != null) {
                    writer.write(BasicStringValue.asString(rightValue));
                }
            } catch (IOException e) {
                throw new BasicRuntimeException(
                        "can not print to the standard output", e);
            }
        }
    }

}

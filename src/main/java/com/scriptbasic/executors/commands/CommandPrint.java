package com.scriptbasic.executors.commands;

import com.scriptbasic.api.ScriptBasicException;
import com.scriptbasic.interfaces.BasicRuntimeException;
import com.scriptbasic.executors.rightvalues.BasicStringValue;
import com.scriptbasic.interfaces.*;

import java.io.IOException;
import java.io.Writer;

public class CommandPrint extends AbstractCommandExpressionListed {

    @Override
    public void execute(final Interpreter interpreter)
            throws ScriptBasicException {
        for (final Expression expression : getExpressionList()) {
            final RightValue rightValue = expression.evaluate(interpreter);
            try {
                final Writer writer = interpreter.getWriter();
                if (writer != null) {
                    writer.write(BasicStringValue.asString(rightValue));
                }
            } catch (final IOException e) {
                throw new BasicRuntimeException(
                        "can not print to the standard output", e);
            }
        }
    }

}

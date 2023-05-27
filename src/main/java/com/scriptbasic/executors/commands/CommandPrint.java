package com.scriptbasic.executors.commands;

import com.scriptbasic.api.ScriptBasicException;
import com.scriptbasic.context.CompilerContext;
import com.scriptbasic.executors.rightvalues.BasicStringValue;
import com.scriptbasic.interfaces.BasicRuntimeException;
import com.scriptbasic.interfaces.Expression;
import com.scriptbasic.spi.Interpreter;

import java.io.IOException;

public class CommandPrint extends AbstractCommandExpressionListed {

    @Override
    public String toJava(CompilerContext cc){
        final var sb = new StringBuilder();
        sb.append("""
                System.out.print(
                """);
        var sp = "" ;
        for (final Expression expression : getExpressionList()) {
            sb.append(sp).append(expression.toJava(cc));
            sp = ",";
        }
        sb.append("""
                );
                _pc++;
                """);
        return sb.toString();
    }

    @Override
    public void execute(final Interpreter interpreter)
            throws ScriptBasicException {
        for (final Expression expression : getExpressionList()) {
            final var rightValue = expression.evaluate(interpreter);
            try {
                final var writer = interpreter.getOutput();
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

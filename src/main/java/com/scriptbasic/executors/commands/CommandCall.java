package com.scriptbasic.executors.commands;

import com.scriptbasic.api.ScriptBasicException;
import com.scriptbasic.executors.rightvalues.FunctionCall;
import com.scriptbasic.api.ScriptBasicException;
import com.scriptbasic.interfaces.Interpreter;

public class CommandCall extends AbstractCommandExpressionListed {
    private final FunctionCall functionCall;

    public CommandCall(final FunctionCall functionCall) {
        this.functionCall = functionCall;
    }

    @Override
    public void execute(final Interpreter interpreter)
            throws ScriptBasicException {
        functionCall.evaluate(interpreter);
    }

}

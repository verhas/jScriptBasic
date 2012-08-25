package com.scriptbasic.executors.commands;

import com.scriptbasic.executors.rightvalues.FunctionCall;
import com.scriptbasic.interfaces.ExecutionException;
import com.scriptbasic.interfaces.ExtendedInterpreter;

public class CommandCall extends AbstractCommandExpressionListed {
    private FunctionCall functionCall;

    /**
     * @return the functionCall
     */
    public FunctionCall getFunctionCall() {
        return functionCall;
    }

    /**
     * @param functionCall
     *            the functionCall to set
     */
    public void setFunctionCall(FunctionCall functionCall) {
        this.functionCall = functionCall;
    }

    @Override
    public void execute(final ExtendedInterpreter interpreter)
            throws ExecutionException {
        functionCall.evaluate(interpreter);
    }

}

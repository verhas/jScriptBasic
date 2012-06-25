package com.scriptbasic.interfaces;

public interface Executor {
    /**
     * 
     * @param interpreter
     */
    void execute(ExtendedInterpreter interpreter) throws ExecutionException;
}

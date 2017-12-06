package com.scriptbasic.interfaces;

public interface Executor {
    /**
     * @param interpreter
     */
    void execute(Interpreter interpreter) throws ExecutionException;
}

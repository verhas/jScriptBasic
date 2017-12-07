package com.scriptbasic.interfaces;

import com.scriptbasic.api.ScriptBasicException;

public interface Executor {
    /**
     * @param interpreter
     */
    void execute(Interpreter interpreter) throws ScriptBasicException;
}

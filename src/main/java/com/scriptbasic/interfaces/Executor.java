package com.scriptbasic.interfaces;

import com.scriptbasic.api.ScriptBasicException;
import com.scriptbasic.spi.Interpreter;

public interface Executor {
    /**
     * @param interpreter parameter
     * @throws ScriptBasicException in case there was an exception
     */
    void execute(Interpreter interpreter) throws ScriptBasicException;
}

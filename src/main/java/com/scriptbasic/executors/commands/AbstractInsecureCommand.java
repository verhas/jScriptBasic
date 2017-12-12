package com.scriptbasic.executors.commands;

import com.scriptbasic.api.ScriptBasicException;
import com.scriptbasic.spi.Interpreter;

import java.util.Optional;

public abstract class AbstractInsecureCommand extends AbstractCommand {
    private static final String CONFIG_KEY_INSECURE = "insecure";
    private boolean allowed = false;
    private boolean configured = false;

    protected void assertInsecure(final Interpreter interpreter) throws ScriptBasicException {
        if (!configured) {
            configured = true;
            Optional<String> useAllowed = interpreter.getConfiguration().getConfigValue(CONFIG_KEY_INSECURE);
            if (useAllowed.isPresent() && useAllowed.get().equals("true")) {
                allowed = true;
            }
        }
        if( ! allowed ){
            throw new ScriptBasicException("Insecure mode is not configured");
        }
    }
}

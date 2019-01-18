package com.scriptbasic.hooks;

import com.scriptbasic.api.Configuration;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import javax.script.ScriptEngineManager;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Peter Verhas
 * date Aug 5, 2012
 */

public class TestRunLimitHook {

    private static void setConfig(final Configuration config) {
        config.set("hook.0", "com.scriptbasic.hooks.RunLimitHook");
        config.set("RunLimitHook.stepLimit", "10");
        config.set("RunLimitHook.timeLimitMillis", "100");
    }


    @Test
    public void testLimitHookSteps() throws Exception {
        final var scriptEngineManager = new ScriptEngineManager();
        final var scriptEngine = scriptEngineManager.getEngineByExtension("sb");
        assertNotNull(scriptEngine);
        assertTrue(scriptEngine instanceof com.scriptbasic.script.ScriptEngine);
        final var config = ((com.scriptbasic.script.ScriptEngine) scriptEngine).ctx.configuration;
        setConfig(config);
        config.set("RunLimitHook.timeLimitMillis", "1000000000");
        try {
            scriptEngine.eval("while true\nwend\n");
            Assertions.fail("infinite loop did not throw exception");
        } catch (final RuntimeException e) {
            assertEquals("The code exceeded the maximum number of steps", e.getMessage());
        }
    }

    @Test
    public void testLimitHookTime() throws Exception {
        final var scriptEngineManager = new ScriptEngineManager();
        final var scriptEngine = scriptEngineManager.getEngineByExtension("sb");
        assertNotNull(scriptEngine);
        assertTrue(scriptEngine instanceof com.scriptbasic.script.ScriptEngine);
        final var config = ((com.scriptbasic.script.ScriptEngine) scriptEngine).ctx.configuration;
        setConfig(config);
        config.set("RunLimitHook.stepLimit=100000000");
        try {
            scriptEngine.eval("while true\nwend\n");
            Assertions.fail("infinite loop did not throw exception");
        } catch (final RuntimeException e) {
            assertEquals("The code exceeded the maximum allowed time", e.getMessage());
        }
    }
}

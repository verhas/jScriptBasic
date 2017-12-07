package com.scriptbasic.hooks;

import com.scriptbasic.api.Configuration;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;

import static org.junit.Assert.*;

/**
 * @author Peter Verhas
 * date Aug 5, 2012
 */

public class TestRunLimitHook {

    private static void setConfig(Configuration config) {
        config.set("hook.0", "com.scriptbasic.hooks.RunLimitHook");
        config.set("RunLimitHook.stepLimit", "10");
        config.set("RunLimitHook.timeLimitMillis", "100");
    }

    @After
    public void tearDown() {

    }

    @Before
    public void setUp() {
    }

    @Test
    public void testLimitHookSteps() throws Exception {
        ScriptEngineManager scriptEngineManager = new ScriptEngineManager();
        ScriptEngine scriptEngine = scriptEngineManager.getEngineByExtension("sb");
        assertNotNull(scriptEngine);
        assertTrue(scriptEngine instanceof com.scriptbasic.script.ScriptEngine);
        Configuration config = ((com.scriptbasic.script.ScriptEngine) scriptEngine).ctx.configuration;
        setConfig(config);
        try {
            scriptEngine.eval("while true\nwend\n");
            Assert.fail("infinite loop did not throw exception");
        } catch (RuntimeException e) {
            assertEquals("The code exceeded the maximum number of steps", e.getMessage());
        }
    }

    @Test
    public void testLimitHookTime() throws Exception {
        ScriptEngineManager scriptEngineManager = new ScriptEngineManager();
        ScriptEngine scriptEngine = scriptEngineManager.getEngineByExtension("sb");
        assertNotNull(scriptEngine);
        assertTrue(scriptEngine instanceof com.scriptbasic.script.ScriptEngine);
        Configuration config = ((com.scriptbasic.script.ScriptEngine) scriptEngine).ctx.configuration;
        setConfig(config);
        config.set("RunLimitHook.stepLimit=100000000");
        try {
            scriptEngine.eval("while true\nwend\n");
            Assert.fail("infinite loop did not throw exception");
        } catch (RuntimeException e) {
            assertEquals("The code exceeded the maximum allowed time", e.getMessage());
        }
    }
}

package com.scriptbasic.hooks;

import com.scriptbasic.interfaces.Configuration;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import java.util.Properties;

import static org.junit.Assert.*;

/**
 * @author Peter Verhas
 * date Aug 5, 2012
 */

public class TestRunLimitHook {

    private static Properties setConfig(Configuration config) {
        Properties configProperties = new Properties();
        configProperties.put("hook.0", "com.scriptbasic.hooks.RunLimitHook");
        configProperties.put("RunLimitHook.stepLimit", "10");
        configProperties.put("RunLimitHook.timeLimitMillis", "100");

        config.setConfigProperties(configProperties);
        return configProperties;
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
        assertTrue(scriptEngine instanceof com.scriptbasic.api.script.ScriptEngine);
        Configuration config = ((com.scriptbasic.api.script.ScriptEngine) scriptEngine).ctx.configuration;
        setConfig(config);
        Properties p = setConfig(config);
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
        assertTrue(scriptEngine instanceof com.scriptbasic.api.script.ScriptEngine);
        Configuration config = ((com.scriptbasic.api.script.ScriptEngine) scriptEngine).ctx.configuration;
        setConfig(config);
        Properties p = setConfig(config);
        p.put("RunLimitHook.stepLimit", "100000000");
        try {
            scriptEngine.eval("while true\nwend\n");
            Assert.fail("infinite loop did not throw exception");
        } catch (RuntimeException e) {
            assertEquals("The code exceeded the maximum allowed time", e.getMessage());
        }
    }
}

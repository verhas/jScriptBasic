package com.scriptbasic.hooks;

import com.scriptbasic.configuration.BasicConfiguration;
import com.scriptbasic.interfaces.Configuration;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import java.util.Properties;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * @author Peter Verhas
 * date Aug 5, 2012
 * 
 */

public class TestRunLimitHook {

	@After
	public void tearDown() {

	}

	@Before
	public void setUp() {
	}

	private static Properties setConfig(Configuration config) {
		Properties configProperties = new Properties();
		configProperties.put("hook.0", "com.scriptbasic.hooks.RunLimitHook");
		configProperties.put("RunLimitHook.stepLimit", "10");
		configProperties.put("RunLimitHook.timeLimitMillis", "100");

		config.setConfigProperties(configProperties);
		return configProperties;
	}

	@SuppressWarnings("static-method")
	@Test
	public void testLimitHookSteps() throws Exception {
		ScriptEngineManager scriptEngineManager = new ScriptEngineManager();
		ScriptEngine scriptEngine = scriptEngineManager.getEngineByExtension("sb");
		assertNotNull(scriptEngine);
		assertTrue(scriptEngine instanceof com.scriptbasic.api.script.ScriptEngine);
		com.scriptbasic.api.script.ScriptEngine bse = (com.scriptbasic.api.script.ScriptEngine) scriptEngine;
		Configuration config = new BasicConfiguration();
		setConfig(config);
		assertNotNull(scriptEngine);
		try {
			scriptEngine.eval("while true\nwend\n");
			Assert.fail("infinite loop did not throw exception");
		} catch (RuntimeException e) {
			Assert.assertEquals(
					"The code exceeded the maximum number of steps",
					e.getMessage());
		}
	}

	@SuppressWarnings("static-method")
	@Test
	public void testLimitHookTime() throws Exception {
		ScriptEngineManager scriptEngineManager = new ScriptEngineManager();
		ScriptEngine scriptEngine = scriptEngineManager.getEngineByExtension("sb");
		assertNotNull(scriptEngine);
		assertTrue(scriptEngine instanceof com.scriptbasic.api.script.ScriptEngine);
		com.scriptbasic.api.script.ScriptEngine bse = (com.scriptbasic.api.script.ScriptEngine) scriptEngine;
		Configuration config = new BasicConfiguration();
		Properties p = setConfig(config);
		p.put("RunLimitHook.stepLimit", "100000000");
		assertNotNull(scriptEngine);
		try {
			scriptEngine.eval("while true\nwend\n");
			Assert.fail("infinite loop did not throw exception");
		} catch (RuntimeException e) {
			Assert.assertEquals("The code exceeded the maximum allowed time",
					e.getMessage());
		}
	}
}

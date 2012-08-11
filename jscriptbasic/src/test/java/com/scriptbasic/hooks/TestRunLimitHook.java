/**
 *
 */
package com.scriptbasic.hooks;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import java.util.Properties;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import com.scriptbasic.interfaces.Configuration;
import com.scriptbasic.interfaces.Factory;
import com.scriptbasic.utility.FactoryUtility;
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
        ScriptEngineManager sem = new ScriptEngineManager();
        ScriptEngine se = sem.getEngineByExtension("sb");
        assertNotNull(se);
        assertTrue(se instanceof com.scriptbasic.javax.script.ScriptEngine);
        com.scriptbasic.javax.script.ScriptEngine bse = (com.scriptbasic.javax.script.ScriptEngine) se;
        Factory factory = bse.getBasicFactory();
        Configuration config = FactoryUtility.getConfiguration(factory);
        setConfig(config);
        assertNotNull(se);
        try {
            se.eval("while true\nwend\n");
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
        ScriptEngineManager sem = new ScriptEngineManager();
        ScriptEngine se = sem.getEngineByExtension("sb");
        assertNotNull(se);
        assertTrue(se instanceof com.scriptbasic.javax.script.ScriptEngine);
        com.scriptbasic.javax.script.ScriptEngine bse = (com.scriptbasic.javax.script.ScriptEngine) se;
        Factory factory = bse.getBasicFactory();
        Configuration config = FactoryUtility.getConfiguration(factory);
        Properties p = setConfig(config);
        p.put("RunLimitHook.stepLimit", "100000000");
        assertNotNull(se);
        try {
            se.eval("while true\nwend\n");
            Assert.fail("infinite loop did not throw exception");
        } catch (RuntimeException e) {
            Assert.assertEquals(
                    "The code exceeded the maximum allowed time",
                    e.getMessage());
        }
    }
}
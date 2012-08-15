/**
 * 
 */
package com.scriptbasic.javax.script;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.io.StringWriter;
import java.util.List;
import java.util.Properties;

import javax.script.Bindings;
import javax.script.ScriptContext;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineFactory;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

import org.junit.After;
import org.junit.Test;

import com.scriptbasic.Version;
import com.scriptbasic.factories.FactoryServiceLoader;
import com.scriptbasic.factories.SingletonFactoryFactory;
import com.scriptbasic.interfaces.Configuration;
import com.scriptbasic.utility.ReflectionUtility;
import com.scriptbasic.utility.FactoryUtility;

/**
 * @author Peter Verhas
 * @date Jul 30, 2012
 * 
 */

public class TestJavaxInterface {

    @SuppressWarnings("static-method")
    @After
    public void tearDown() {
        try {
            ReflectionUtility.setField(SingletonFactoryFactory.class, "singleton",
                    FactoryServiceLoader.loadFactory());
        } catch (NoSuchFieldException | SecurityException
                | IllegalArgumentException | IllegalAccessException e) {
            e.printStackTrace();
            return;
        }
    }

    public void setUp() {

    }

    @SuppressWarnings("static-method")
    @Test
    public void testCoverConfiguration() throws Exception {
        Configuration config = FactoryUtility
                .getConfiguration(SingletonFactoryFactory.getFactory());
        Properties configProperties = new Properties();
        configProperties.put("extension.0", "b0");
        configProperties.put("extension.1", "b1");
        configProperties.put("extension.2", "b2");
        configProperties.put("extension.3", "b3");
        configProperties.put("extension.4", "b4");
        configProperties.put("mimeType.0", "mt0");
        configProperties.put("mimeType.1", "mt1");
        configProperties.put("mimeType.2", "mt2");
        configProperties.put("mimeType.3", "mt3");
        configProperties.put("mimeType.4", "mt4");
        configProperties.put("mimeType.5", "mt5");
        configProperties.put("name.0", "n0");
        configProperties.put("name.1", "n1");
        configProperties.put("engineName", "engineNameScriptBasic");
        configProperties.put("version", "666");
        configProperties.put("language", "esperanto");
        configProperties.put("languageVersion", "ancient");

        config.setConfigProperties(configProperties);
        ScriptEngineFactory sef = new com.scriptbasic.javax.script.ScriptEngineFactory();
        assertEquals("esperanto", sef.getLanguageName());
        assertEquals("ancient", sef.getLanguageVersion());
        assertEquals(2, sef.getNames().size());
        assertEquals(6, sef.getMimeTypes().size());
        assertEquals(5, sef.getExtensions().size());
        ScriptEngine se = sef.getScriptEngine();
        assertNotNull(se);
        assertTrue(se instanceof com.scriptbasic.javax.script.ScriptEngine);
    }

    @SuppressWarnings("static-method")
    @Test
    public void testRun1() throws ScriptException, IOException {
        ScriptEngineManager sem = new ScriptEngineManager();
        List<ScriptEngineFactory> sefs = sem.getEngineFactories();
        boolean sbWasFound = false;
        for (ScriptEngineFactory sef : sefs) {
            if (sef.getEngineName() != null
                    && sef.getEngineName().equals(Version.engineName)) {
                sbWasFound = true;
                assertEquals(Version.language, sef.getLanguageName());
                assertEquals(Version.languageVersion, sef.getLanguageVersion());
                assertEquals(Version.names.size(), sef.getNames().size());
                assertEquals(Version.mimeTypes.size(), sef.getMimeTypes()
                        .size());
                assertEquals(Version.extensions.size(), sef.getExtensions()
                        .size());
            }
        }
        assertTrue(sbWasFound);
        FactoryUtility.getConfiguration(SingletonFactoryFactory.getFactory());
        ScriptEngine se = sem.getEngineByExtension("sb");
        assertNotNull(se);
        assertTrue(se instanceof com.scriptbasic.javax.script.ScriptEngine);
        assertNotNull(se);
        se.eval("print \"first script\"");
        ScriptContext context = se.getContext();
        Bindings bindings = context.getBindings(ScriptContext.ENGINE_SCOPE);
        bindings.put("B", new Integer(13));
        bindings.put("A", null);
        StringWriter writer = new StringWriter();
        context.setWriter(writer);
        se.eval("A = B\nprint \"hiha\"", context);
        Long z = (Long) bindings.get("A");
        assertEquals(new Long(13), z);
        assertEquals("hiha", writer.toString());
    }
}

/**
 *
 */
package com.scriptbasic.javax.script;

import com.scriptbasic.api.Version;
import com.scriptbasic.factories.FactoryServiceLoader;
import com.scriptbasic.factories.SingletonFactoryFactory;
import com.scriptbasic.interfaces.Configuration;
import com.scriptbasic.utility.FactoryUtility;
import org.junit.After;
import org.junit.Assert;
import org.junit.Test;

import javax.script.*;
import java.io.IOException;
import java.io.StringWriter;
import java.lang.reflect.Field;
import java.util.List;
import java.util.Properties;

import static org.junit.Assert.*;

/**
 * @author Peter Verhas
 * date Jul 30, 2012
 */

public class TestJavaxInterface {

    @SuppressWarnings("static-method")
    @After
    public void tearDown() {
        try {
            Field field = SingletonFactoryFactory.class.getDeclaredField("singleton");
            field.setAccessible(true);
            field.set(null, FactoryServiceLoader.loadFactory());
        } catch (Exception e) {
            e.printStackTrace();
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
        ScriptEngineFactory sef = new com.scriptbasic.api.script.ScriptEngineFactory();
        assertEquals("esperanto", sef.getLanguageName());
        assertEquals("ancient", sef.getLanguageVersion());
        assertEquals(2, sef.getNames().size());
        assertEquals(6, sef.getMimeTypes().size());
        assertEquals(5, sef.getExtensions().size());
        ScriptEngine se = sef.getScriptEngine();
        assertNotNull(se);
        assertTrue(se instanceof com.scriptbasic.api.script.ScriptEngine);
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
        assertTrue(se instanceof com.scriptbasic.api.script.ScriptEngine);
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

    @Test
    public void testQueries() {
        com.scriptbasic.api.script.ScriptEngineFactory sef = new com.scriptbasic.api.script.ScriptEngineFactory();
        Bindings b = new SimpleBindings();
        sef.setGlobalScopeBinding(b);
        Assert.assertEquals(sef.getGlobalScopeBinding(), b);
        sef.getEngineVersion();
        sef.getParameter(ScriptEngine.ENGINE);
        sef.getParameter(ScriptEngine.ENGINE_VERSION);
        sef.getParameter(ScriptEngine.NAME);
        sef.getParameter(ScriptEngine.LANGUAGE);
        sef.getParameter(ScriptEngine.LANGUAGE_VERSION);
        sef.getParameter("THREADING");
        Assert.assertNull(sef.getParameter("abrakadabra"));
        sef.getMethodCallSyntax(null, "method", "a", "b", "c");
        sef.getMethodCallSyntax(null, "method");
        sef.getOutputStatement("hello word");
        sef.getProgram("rem", "rem");
    }
}

package com.scriptbasic.javax.script;

import com.scriptbasic.api.Version;
import com.scriptbasic.script.ScriptBasicEngineFactory;
import com.scriptbasic.configuration.BasicConfiguration;
import org.junit.Assert;
import org.junit.Test;

import javax.script.*;
import java.io.IOException;
import java.io.StringWriter;
import java.util.List;
import java.util.Properties;

import static org.junit.Assert.*;

/**
 * @author Peter Verhas
 * date Jul 30, 2012
 */

public class TestJavaxInterface {

    @Test
    public void testCoverConfiguration() {
        final var configProperties = new Properties();
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
        final var config = new BasicConfiguration();
        config.setConfigProperties(configProperties);
        final var factory = new ScriptBasicEngineFactory(config);
        assertEquals("esperanto", factory.getLanguageName());
        assertEquals("ancient", factory.getLanguageVersion());
        assertEquals(2, factory.getNames().size());
        assertEquals(6, factory.getMimeTypes().size());
        assertEquals(5, factory.getExtensions().size());
        final var se = factory.getScriptEngine();
        assertNotNull(se);
        assertTrue(se instanceof com.scriptbasic.script.ScriptEngine);
    }


    @Test
    public void testRun1() throws ScriptException {
        final var manager = new ScriptEngineManager();
        final List<ScriptEngineFactory> factories = manager.getEngineFactories();
        boolean sbWasFound = false;
        for (final ScriptEngineFactory factory : factories) {
            if (factory.getEngineName() != null
                    && factory.getEngineName().equals(Version.engineName)) {
                sbWasFound = true;
                assertEquals(Version.language, factory.getLanguageName());
                assertEquals(Version.languageVersion, factory.getLanguageVersion());
                assertEquals(Version.names.size(), factory.getNames().size());
                assertEquals(Version.mimeTypes.size(), factory.getMimeTypes()
                        .size());
                assertEquals(Version.extensions.size(), factory.getExtensions()
                        .size());
            }
        }
        assertTrue(sbWasFound);

        final var se = manager.getEngineByExtension("sb");
        assertNotNull(se);
        assertTrue(se instanceof com.scriptbasic.script.ScriptEngine);
        se.eval("print \"first script\"");
        final var context = se.getContext();
        final var bindings = context.getBindings(ScriptContext.ENGINE_SCOPE);
        bindings.put("B", 13);
        bindings.put("A", null);
        final var writer = new StringWriter();
        context.setWriter(writer);
        se.eval("A = B\nprint \"hiha\"", context);
        final var z = (Long) bindings.get("A");
        assertEquals(Long.valueOf(13), z);
        assertEquals("hiha", writer.toString());
    }

    @Test
    public void testQueries() {
        final var sef = new ScriptBasicEngineFactory();
        final var b = new SimpleBindings();
        sef.setGlobalScopeBinding(b);
        Assert.assertEquals(sef.getGlobalScopeBinding(), b);
        //noinspection ResultOfMethodCallIgnored
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

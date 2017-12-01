package com.scriptbasic.api.script;

import com.scriptbasic.api.Version;
import com.scriptbasic.configuration.BasicConfiguration;
import com.scriptbasic.interfaces.Configuration;
import com.scriptbasic.log.Logger;
import com.scriptbasic.log.LoggerFactory;

import javax.script.Bindings;
import javax.script.ScriptEngine;
import java.util.List;
import java.util.function.Consumer;

/**
 * @author Peter Verhas
 * date Jul 26, 2012
 */
public class ScriptBasicEngineFactory implements javax.script.ScriptEngineFactory {
    private static final Logger LOG = LoggerFactory.getLogger();
    // configuration is more or less factory independent, at least the
    // standard scripting interface does not provide any mean to define a
    // specific interface for the different engine instances that may
    // concurrently exist in the JVM
    public final Configuration config;
    private Bindings globalScopeBinding;
    private String engineName = Version.engineName;
    private String version = Version.version;
    private List<String> extensions = Version.extensions;
    private List<String> mimeTypes = Version.mimeTypes;
    private List<String> names = Version.names;
    private String language = Version.language;
    private String languageVersion = Version.languageVersion;

    public ScriptBasicEngineFactory() {
        this(new BasicConfiguration());
    }
    /**
     * The constructor reads the configuration and fills the constants that are
     * requested by the {@link javax.script.ScriptEngineManager}.
     */
    public ScriptBasicEngineFactory(Configuration config) {
        this.config = config;
        engineName = config.getConfigValue("engineName").orElse(engineName);
        version = config.getConfigValue("version").orElse(version);
        language = config.getConfigValue("language").orElse(language);
        languageVersion = config.getConfigValue("languageVersion").orElse(languageVersion);

        loadKeys("extension", x -> extensions = x);
        loadKeys("mimeType", x -> mimeTypes = x);
        loadKeys("name", x -> names = x);

    }

    /**
     * @return the globalScopeBinding
     */
    public Bindings getGlobalScopeBinding() {
        return globalScopeBinding;
    }

    /**
     * @param globalScopeBinding the globalScopeBinding to set
     */
    public void setGlobalScopeBinding(Bindings globalScopeBinding) {
        this.globalScopeBinding = globalScopeBinding;
    }

    /**
     * Load the configuration keys 'key.0', 'key.1' and so on, and store them using the 'setter'.
     *
     * @param key    the name of the configuration key.
     * @param setter consumer that uses the list of values from the configuration
     */
    private void loadKeys(String key, Consumer<List<String>> setter) {
        if (config != null) {
            List<String> list = config.getConfigValueList(key);
            if (!list.isEmpty()) {
                setter.accept(list);
            }
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see javax.script.ScriptEngineFactory#getEngineName()
     */
    @Override
    public String getEngineName() {
        return engineName;
    }

    /*
     * (non-Javadoc)
     *
     * @see javax.script.ScriptEngineFactory#getEngineVersion()
     */
    @Override
    public String getEngineVersion() {
        return version;
    }

    /*
     * (non-Javadoc)
     *
     * @see javax.script.ScriptEngineFactory#getExtensions()
     */
    @Override
    public List<String> getExtensions() {
        return extensions;
    }

    /*
     * (non-Javadoc)
     *
     * @see javax.script.ScriptEngineFactory#getMimeTypes()
     */
    @Override
    public List<String> getMimeTypes() {
        return mimeTypes;
    }

    /*
     * (non-Javadoc)
     *
     * @see javax.script.ScriptEngineFactory#getNames()
     */
    @Override
    public List<String> getNames() {
        return names;
    }

    /*
     * (non-Javadoc)
     *
     * @see javax.script.ScriptEngineFactory#getLanguageName()
     */
    @Override
    public String getLanguageName() {
        return language;
    }

    /*
     * (non-Javadoc)
     *
     * @see javax.script.ScriptEngineFactory#getLanguageVersion()
     */
    @Override
    public String getLanguageVersion() {
        return languageVersion;
    }

    /*
     * (non-Javadoc)
     *
     * @see javax.script.ScriptEngineFactory#getParameter(java.lang.String)
     */
    @Override
    public Object getParameter(String key) {
        if (key.equals(ScriptEngine.ENGINE))
            return getEngineName();
        if (key.equals(ScriptEngine.ENGINE_VERSION))
            return getEngineVersion();
        if (key.equals(ScriptEngine.NAME))
            return getNames();
        if (key.equals(ScriptEngine.LANGUAGE))
            return getLanguageName();
        if (key.equals(ScriptEngine.LANGUAGE_VERSION))
            return getLanguageVersion();
        if (key.equals("THREADING"))
            return "STATELESS";
        return null;
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * javax.script.ScriptEngineFactory#getMethodCallSyntax(java.lang.String,
     * java.lang.String, java.lang.String[])
     */
    @Override
    public String getMethodCallSyntax(String obj, String m, String... args) {
        String sep = "";
        String argsS = "";
        for (String arg : args) {
            argsS = sep + arg;
            sep = ",";
        }
        return String.format("%s.%s(%s)", obj, m, argsS);
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * javax.script.ScriptEngineFactory#getOutputStatement(java.lang.String)
     */
    @Override
    public String getOutputStatement(String toDisplay) {
        return String.format("print \"%s\"", toDisplay);
    }

    /*
     * (non-Javadoc)
     *
     * @see javax.script.ScriptEngineFactory#getProgram(java.lang.String[])
     */
    @Override
    public String getProgram(String... statements) {
        int len = 0;
        for (String line : statements) {
            len += 1 + line.length();
        }
        StringBuilder sb = new StringBuilder(len);
        for (String line : statements) {
            sb.append(line).append("\n");
        }
        return sb.toString();
    }

    /*
     * (non-Javadoc)
     *
     * @see javax.script.ScriptEngineFactory#getScriptEngine()
     */
    @Override
    public ScriptEngine getScriptEngine() {
        return new com.scriptbasic.api.script.ScriptEngine(this);
    }

}

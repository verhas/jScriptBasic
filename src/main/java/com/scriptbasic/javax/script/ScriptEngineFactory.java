/**
 * 
 */
package com.scriptbasic.javax.script;

import java.util.ArrayList;
import java.util.List;

import javax.script.Bindings;
import javax.script.ScriptEngine;

import com.scriptbasic.Version;
import com.scriptbasic.factories.SingletonFactoryFactory;
import com.scriptbasic.interfaces.Configuration;
import com.scriptbasic.log.Logger;
import com.scriptbasic.log.LoggerFactory;
import com.scriptbasic.utility.ReflectionUtility;
import com.scriptbasic.utility.FactoryUtility;

/**
 * @author Peter Verhas
 * date Jul 26, 2012
 * 
 */
public class ScriptEngineFactory implements javax.script.ScriptEngineFactory {
	private static final Logger LOG = LoggerFactory
			.getLogger(ScriptEngineFactory.class);
	private Bindings globalScopeBinding;

	/**
	 * @return the globalScopeBinding
	 */
	public Bindings getGlobalScopeBinding() {
		return globalScopeBinding;
	}

	/**
	 * @param globalScopeBinding
	 *            the globalScopeBinding to set
	 */
	public void setGlobalScopeBinding(Bindings globalScopeBinding) {
		this.globalScopeBinding = globalScopeBinding;
	}

	private String engineName = Version.engineName;
	private String version = Version.version;
	private List<String> extensions = Version.extensions;
	private List<String> mimeTypes = Version.mimeTypes;
	private List<String> names = Version.names;
	private String language = Version.language;
	private String languageVersion = Version.languageVersion;
	// configuration is more or less factory independent, at least the
	// standard scripting interface does not provide any mean to define a
	// specific interface for the different engine instances that may
	// concurrently exist in the JVM
	private Configuration config = FactoryUtility
			.getConfiguration(SingletonFactoryFactory.getFactory());

	/**
	 * Load the configuration keys into the private fields. The parameters
	 * requested by the {@link ScriptEngineManager}, {@code names},
	 * {@code mimeTypes}, {@code extensions} are stored in private
	 * {@code List<String>}fields in this class and returned by the methods
	 * defined by the interface {@link javax.script.ScriptEngineFactory}.
	 * <p>
	 * Unless configured these fields are filled in the object constructor from
	 * the constants defined in the class {@link Version}.
	 * <p>
	 * When the configuration defines these values then they replace the values
	 * defined in the class {@code Version}.
	 * <p>
	 * The configuration key (used as a parameter to this method) should be the
	 * same as the name of the field, in singular format. Thus {@code key} can
	 * be {@code extension}, {@code name}, {@code mimeType}.
	 * <p>
	 * The configuration should contain the properties '{@code key}'s'{@code .n}
	 * ' for n=0..z
	 * <p>
	 * For example
	 * 
	 * <pre>
	 * extension.0=sb
	 * extension.1=bas
	 * extension.2=scriba
	 * </pre>
	 * 
	 * will define the default two extensions and an extra one. The numbers
	 * start from zero and should be continuous. Empty values are skipped, so
	 * deleting one value you need not reorder or renumber the whole list in
	 * your configuration.
	 * <p>
	 * If a list is defined in the configuration then the default values are
	 * dropped totally. Thus if you for example want to use the default
	 * extensions as well as the new, non-standard extension {@code scriba},
	 * then you have to list all three extensions.
	 * 
	 * @param key
	 *            the name of the configuration key. Note that the field should
	 *            have the name {@code key}'s', that is the plural form of the
	 *            name of the configuration key.
	 */
	private void loadKeys(String key) {
		final String keys = key + "s";
		if (config != null && config.getConfigValue(key + "." + 0) != null) {
			try {
				List<String> list = new ArrayList<String>();
				ReflectionUtility.setField(this, keys, list);
				for (int i = 0; config.getConfigValue(key + "." + i) != null; i++) {
					if (config.getConfigValue(key + "." + i).length() > 0) {
						list.add(config.getConfigValue(key));
					}
				}
			} catch (Exception e) {
				LOG.error(
						"Can not intialize the configuration parameters from configuration",
						e);
			}
		}
	}

	/**
	 * The constructor reads the configuration and fills the constants that are
	 * requested by the {@link ScriptEngineManager}.
	 */
	public ScriptEngineFactory() {

		if (config.getConfigValue("engineName") != null) {
			engineName = config.getConfigValue("engineName");
		}
		if (config.getConfigValue("version") != null) {
			version = config.getConfigValue("version");
		}
		if (config.getConfigValue("language") != null) {
			language = config.getConfigValue("language");
		}
		if (config.getConfigValue("languageVersion") != null) {
			languageVersion = config.getConfigValue("languageVersion");
		}

		loadKeys("extension");
		loadKeys("mimeType");
		loadKeys("name");

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
		return new com.scriptbasic.javax.script.ScriptEngine(this);
	}

}

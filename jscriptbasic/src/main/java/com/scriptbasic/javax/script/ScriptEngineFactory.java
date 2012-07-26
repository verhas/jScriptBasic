/**
 * 
 */
package com.scriptbasic.javax.script;

import java.util.List;

import javax.script.ScriptEngine;

import com.scriptbasic.Version;

/**
 * @author Peter Verhas
 * @date Jul 26, 2012
 * 
 */
public class ScriptEngineFactory implements javax.script.ScriptEngineFactory {

    /*
     * (non-Javadoc)
     * 
     * @see javax.script.ScriptEngineFactory#getEngineName()
     */
    @Override
    public String getEngineName() {
        return "scriptbasic";
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.script.ScriptEngineFactory#getEngineVersion()
     */
    @Override
    public String getEngineVersion() {
        return Version.version;
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.script.ScriptEngineFactory#getExtensions()
     */
    @Override
    public List<String> getExtensions() {
        return Version.extensions;
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.script.ScriptEngineFactory#getMimeTypes()
     */
    @Override
    public List<String> getMimeTypes() {
        return Version.mimeTypes;
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.script.ScriptEngineFactory#getNames()
     */
    @Override
    public List<String> getNames() {
        return Version.names;
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.script.ScriptEngineFactory#getLanguageName()
     */
    @Override
    public String getLanguageName() {
        return Version.language;
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.script.ScriptEngineFactory#getLanguageVersion()
     */
    @Override
    public String getLanguageVersion() {
        return Version.languageVersion;
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.script.ScriptEngineFactory#getParameter(java.lang.String)
     */
    @Override
    public Object getParameter(String key) {
        switch (key) {
        case ScriptEngine.ENGINE:
            return getEngineName();
        case ScriptEngine.ENGINE_VERSION:
            return getEngineVersion();
        case ScriptEngine.NAME:
            return getNames();
        case ScriptEngine.LANGUAGE:
            return getLanguageName();
        case ScriptEngine.LANGUAGE_VERSION:
            return getLanguageVersion();
        case "THREADING":
            return "STATELESS";
        }
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
        return String.format("%s.%s(%s)", obj, argsS);
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
        for( String line : statements){
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

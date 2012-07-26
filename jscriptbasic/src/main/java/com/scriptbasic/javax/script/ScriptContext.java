/**
 * 
 */
package com.scriptbasic.javax.script;

import java.io.Reader;
import java.io.Writer;
import java.util.LinkedList;
import java.util.List;

import javax.script.Bindings;

/**
 * @author Peter Verhas
 * @date Jul 26, 2012
 * 
 */
public class ScriptContext implements javax.script.ScriptContext {
    private Bindings globalScopeBinding;
    private Bindings engineScopeBindings;

    /*
     * (non-Javadoc)
     * 
     * @see javax.script.ScriptContext#setBindings(javax.script.Bindings, int)
     */
    @Override
    public void setBindings(Bindings bindings, int scope) {
        switch (scope) {
        case ScriptContext.ENGINE_SCOPE:
            engineScopeBindings = bindings;
            break;
        case ScriptContext.GLOBAL_SCOPE:
            globalScopeBinding = bindings;
            break;
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.script.ScriptContext#getBindings(int)
     */
    @Override
    public Bindings getBindings(int scope) {
        switch (scope) {
        case ScriptContext.ENGINE_SCOPE:
            return engineScopeBindings;

        case ScriptContext.GLOBAL_SCOPE:
            return globalScopeBinding;
        default:
            return null;
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.script.ScriptContext#setAttribute(java.lang.String,
     * java.lang.Object, int)
     */
    @Override
    public void setAttribute(String name, Object value, int scope) {
        getBindings(scope).put(name, value);
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.script.ScriptContext#getAttribute(java.lang.String, int)
     */
    @Override
    public Object getAttribute(String name, int scope) {
        return getBindings(scope).get(name);
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.script.ScriptContext#removeAttribute(java.lang.String, int)
     */
    @Override
    public Object removeAttribute(String name, int scope) {
        return getBindings(scope).remove(name);
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.script.ScriptContext#getAttribute(java.lang.String)
     */
    @Override
    public Object getAttribute(String name) {
        Object value = getBindings(ENGINE_SCOPE).get(name);
        if (value == null) {
            value = getBindings(GLOBAL_SCOPE).get(name);
        }
        return value;
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.script.ScriptContext#getAttributesScope(java.lang.String)
     */
    @Override
    public int getAttributesScope(String name) {
        int scope = 0;
        if (getBindings(ENGINE_SCOPE).get(name) != null) {
            scope = ENGINE_SCOPE;
        } else {
            if (getBindings(GLOBAL_SCOPE).get(name) != null) {
                scope = GLOBAL_SCOPE;
            }
        }
        return scope;
    }
private Writer writer;
private Writer errorWriter;
private Reader reader;

    /*
     * (non-Javadoc)
     * 
     * @see javax.script.ScriptContext#getWriter()
     */
    @Override
    public Writer getWriter() {
        return writer;
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.script.ScriptContext#getErrorWriter()
     */
    @Override
    public Writer getErrorWriter() {
        return errorWriter;
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.script.ScriptContext#setWriter(java.io.Writer)
     */
    @Override
    public void setWriter(Writer writer) {
        this.writer = writer;
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.script.ScriptContext#setErrorWriter(java.io.Writer)
     */
    @Override
    public void setErrorWriter(Writer writer) {
        errorWriter = writer;

    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.script.ScriptContext#getReader()
     */
    @Override
    public Reader getReader() {
        return reader;
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.script.ScriptContext#setReader(java.io.Reader)
     */
    @Override
    public void setReader(Reader reader) {
        this.reader = reader;
    }

    
    private static final List<Integer> scopes = new LinkedList<>();
    static {
        scopes.add(ENGINE_SCOPE);
        scopes.add(GLOBAL_SCOPE);
    }
    /*
     * (non-Javadoc)
     * 
     * @see javax.script.ScriptContext#getScopes()
     */
    @Override
    public List<Integer> getScopes() {
        return scopes;
    }

}

package com.scriptbasic.script;

import com.scriptbasic.context.Context;
import com.scriptbasic.context.ContextBuilder;
import com.scriptbasic.interfaces.AnalysisException;
import com.scriptbasic.api.ScriptBasicException;
import com.scriptbasic.spi.Interpreter;

import javax.script.*;
import java.io.Reader;
import java.io.StringReader;

/**
 * @author Peter Verhas
 * date Jul 26, 2012
 */
public class ScriptEngine extends AbstractScriptEngine {


    public final ScriptBasicEngineFactory scriptEngineFactory;
    public Context ctx;

    public ScriptEngine(final ScriptBasicEngineFactory factory) {
        ctx = ContextBuilder.newContext();
        ctx.configuration = factory.config;
        scriptEngineFactory = factory;
    }

    private static void unmergeBindings(final Interpreter interpreter,
                                        final Bindings bindings) throws ScriptBasicException {
        for (final String name : bindings.keySet()) {
            bindings.put(name, interpreter.getVariable(name));
        }
    }

    private static void mergeBinding(final Interpreter interpreter,
                                     final Bindings bindings) throws ScriptBasicException {
        for (final String name : bindings.keySet()) {
            interpreter.setVariable(name, bindings.get(name));
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see javax.script.ScriptEngine#eval(java.lang.String,
     * javax.script.ScriptContext)
     */
    @Override
    public Object eval(final String script, final ScriptContext context) throws ScriptException {
        return eval(new StringReader(script), context);
    }

    /*
     * (non-Javadoc)
     *
     * @see javax.script.ScriptEngine#eval(Reader,
     * javax.script.ScriptContext)
     */
    @Override
    public Object eval(final Reader reader, final ScriptContext context) throws ScriptException {
        try {
            ctx = ContextBuilder.from(ctx, reader, context.getReader(), context.getWriter(), context.getErrorWriter());
            mergeBinding(ctx.interpreter, context.getBindings(ScriptContext.GLOBAL_SCOPE));
            mergeBinding(ctx.interpreter, context.getBindings(ScriptContext.ENGINE_SCOPE));
            ctx.interpreter.setProgram(ctx.syntaxAnalyzer.analyze());
            ctx.interpreter.execute();
            unmergeBindings(ctx.interpreter, context.getBindings(ScriptContext.ENGINE_SCOPE));
            unmergeBindings(ctx.interpreter, context.getBindings(ScriptContext.GLOBAL_SCOPE));
        } catch (ScriptBasicException | AnalysisException e) {
            throw new ScriptException(e);
        }
        return null;
    }

    /*
     * (non-Javadoc)
     *
     * @see javax.script.ScriptEngine#createBindings()
     */
    @Override
    public Bindings createBindings() {
        return new SimpleBindings();
    }

    /*
     * (non-Javadoc)
     *
     * @see javax.script.ScriptEngine#getFactory()
     */
    @Override
    public ScriptBasicEngineFactory getFactory() {
        return scriptEngineFactory;
    }
}

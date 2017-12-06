package com.scriptbasic.api.script;

import com.scriptbasic.context.Context;
import com.scriptbasic.context.ContextBuilder;
import com.scriptbasic.interfaces.AnalysisException;
import com.scriptbasic.interfaces.ExecutionException;
import com.scriptbasic.interfaces.ExtendedInterpreter;

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

    public ScriptEngine(final ScriptBasicEngineFactory scriptEngineFactory) {
        ctx = ContextBuilder.newContext();
        ctx.configuration = scriptEngineFactory.config;
        this.scriptEngineFactory = scriptEngineFactory;
    }

    private static void unmergeBindings(final ExtendedInterpreter interpreter,
                                        final Bindings bindings) throws ExecutionException {
        for (final String name : bindings.keySet()) {
            bindings.put(name, interpreter.getVariable(name));
        }
    }

    private static void mergeBinding(final ExtendedInterpreter interpreter,
                                     final Bindings bindings) throws ExecutionException {
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
        } catch (ExecutionException | AnalysisException e) {
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

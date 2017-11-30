package com.scriptbasic.api.script;

import com.scriptbasic.factories.Context;
import com.scriptbasic.factories.ContextBuilder;
import com.scriptbasic.interfaces.AnalysisException;
import com.scriptbasic.interfaces.ExecutionException;
import com.scriptbasic.interfaces.ExtendedInterpreter;

import javax.script.*;
import javax.script.ScriptEngineFactory;
import java.io.Reader;
import java.io.StringReader;

/**
 * @author Peter Verhas
 * date Jul 26, 2012
 */
public class ScriptEngine extends AbstractScriptEngine {


    private Context ctx;
    private ScriptEngineFactory scriptEngineFactory;

    public ScriptEngine(ScriptEngineFactory scriptEngineFactory) {
        this.scriptEngineFactory = scriptEngineFactory;
    }

    private static void unmergeBindings(ExtendedInterpreter interpreter,
                                        Bindings bindings) throws ExecutionException {
        for (String name : bindings.keySet()) {
            bindings.put(name, interpreter.getVariable(name));
        }
    }

    private static void mergeBinding(ExtendedInterpreter interpreter,
                                     Bindings bindings) throws ExecutionException {
        for (String name : bindings.keySet()) {
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
    public Object eval(String script, ScriptContext context) throws ScriptException {
        return eval(new StringReader(script), context);
    }

    /*
     * (non-Javadoc)
     *
     * @see javax.script.ScriptEngine#eval(Reader,
     * javax.script.ScriptContext)
     */
    @Override
    public Object eval(Reader reader, ScriptContext context) throws ScriptException {
        try {
            ctx = ContextBuilder.from(reader, context.getReader(), context.getWriter(), context.getErrorWriter());
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
    public ScriptEngineFactory getFactory() {
        return scriptEngineFactory;
    }
}

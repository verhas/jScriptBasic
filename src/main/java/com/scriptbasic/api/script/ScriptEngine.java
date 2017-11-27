package com.scriptbasic.api.script;

import java.io.Reader;
import java.io.StringReader;
import java.io.Writer;

import javax.script.AbstractScriptEngine;
import javax.script.Bindings;
import javax.script.ScriptContext;
import javax.script.ScriptEngineFactory;
import javax.script.ScriptException;
import javax.script.SimpleBindings;

import com.scriptbasic.factories.FactoryFactory;
import com.scriptbasic.interfaces.AnalysisException;
import com.scriptbasic.interfaces.ExecutionException;
import com.scriptbasic.interfaces.ExtendedInterpreter;
import com.scriptbasic.interfaces.Factory;
import com.scriptbasic.readers.GenericReader;
import com.scriptbasic.utility.FactoryUtility;

/**
 * @author Peter Verhas
 * date Jul 26, 2012
 * 
 */
public class ScriptEngine extends AbstractScriptEngine {

	private Factory factory;

	public Factory getBasicFactory() {
		return factory;
	}

	public ScriptEngine(ScriptEngineFactory scriptEngineFactory) {
		this.scriptEngineFactory = scriptEngineFactory;
		factory = FactoryFactory.getFactory();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.script.ScriptEngine#eval(java.lang.String,
	 * javax.script.ScriptContext)
	 */
	@Override
	public Object eval(String script, ScriptContext context)
			throws ScriptException {
		Reader reader = new StringReader(script);
		return eval(reader, context);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.script.ScriptEngine#eval(java.io.Reader,
	 * javax.script.ScriptContext)
	 */
	@Override
	public Object eval(Reader reader, ScriptContext context)
			throws ScriptException {
		ExtendedInterpreter interpreter = FactoryUtility
				.getExtendedInterpreter(factory);
		try {
			mergeBinding(interpreter,
					context.getBindings(ScriptContext.GLOBAL_SCOPE));
			mergeBinding(interpreter,
					context.getBindings(ScriptContext.ENGINE_SCOPE));
			execute(reader, getContext().getReader(), getContext().getWriter(),
					getContext().getErrorWriter());
			unmergeBindings(interpreter,
					context.getBindings(ScriptContext.ENGINE_SCOPE));
			unmergeBindings(interpreter,
					context.getBindings(ScriptContext.GLOBAL_SCOPE));
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

	private ScriptEngineFactory scriptEngineFactory;

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.script.ScriptEngine#getFactory()
	 */
	@Override
	public ScriptEngineFactory getFactory() {
		return scriptEngineFactory;
	}

	private void execute(Reader r, Reader input, Writer output, Writer error)
			throws AnalysisException, ExecutionException {
		final GenericReader reader = new GenericReader(r,null);
		ExtendedInterpreter interpreter = FactoryUtility
				.getExtendedInterpreter(factory);
		interpreter.setProgram(FactoryUtility.getSyntaxAnalyzer(factory)
				.analyze());
		interpreter.setWriter(output);
		interpreter.setErrorWriter(error);
		interpreter.setReader(input);
		interpreter.execute();
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
}

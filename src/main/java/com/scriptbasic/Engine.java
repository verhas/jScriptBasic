package com.scriptbasic;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.io.Writer;

import com.scriptbasic.factories.FactoryFactory;
import com.scriptbasic.interfaces.AnalysisException;
import com.scriptbasic.interfaces.EngineApi;
import com.scriptbasic.interfaces.ExecutionException;
import com.scriptbasic.interfaces.ExtendedInterpreter;
import com.scriptbasic.interfaces.Factory;
import com.scriptbasic.interfaces.HierarchicalReader;
import com.scriptbasic.interfaces.LexicalAnalyzer;
import com.scriptbasic.interfaces.ScriptBasicException;
import com.scriptbasic.interfaces.SourcePath;
import com.scriptbasic.interfaces.SourceProvider;
import com.scriptbasic.readers.GenericHierarchicalReader;
import com.scriptbasic.readers.GenericReader;
import com.scriptbasic.sourceproviders.BasicSourcePath;
import com.scriptbasic.sourceproviders.FileSourceProvider;
import com.scriptbasic.utility.FactoryUtility;
import com.scriptbasic.utility.RightValueUtility;

public class Engine implements EngineApi {

	private final Factory factory;
	private final ExtendedInterpreter interpreter;

	@Override
	public Factory getBasicFactory() {
		return factory;
	}

	public Engine() {
		factory = FactoryFactory.getFactory();
		interpreter = FactoryUtility.getExtendedInterpreter(factory);
	}

	private Reader input;
	private Writer output;
	private Writer error;

	@Override
	public Reader getInput() {
		return input;
	}

	@Override
	public void setInput(final Reader input) {
		this.input = input;
	}

	@Override
	public Writer getOutput() {
		return output;
	}

	@Override
	public void setOutput(final Writer output) {
		this.output = output;
	}

	@Override
	public Writer getError() {
		return error;
	}

	@Override
	public void setError(final Writer error) {
		this.error = error;
	}

	private void eval(final Reader reader, String fileName,
			SourceProvider sourceProvider) throws ScriptBasicException {
		try {
			final com.scriptbasic.interfaces.Reader sourceReader;
			if (reader == null && sourceProvider != null) {
				sourceReader = sourceProvider.get(fileName);
			} else {
				GenericReader genericReader = new GenericReader();
				genericReader.set(reader);
				genericReader.setSourceProvider(sourceProvider);
				sourceReader = genericReader;
			}
			sourceReader.set((String) fileName);
			final LexicalAnalyzer lexicalAnalyzer = FactoryUtility
					.getLexicalAnalyzer(factory);
			HierarchicalReader hReader = new GenericHierarchicalReader();
			hReader.include(sourceReader);
			lexicalAnalyzer.set(hReader);
			interpreter.setProgram(FactoryUtility.getSyntaxAnalyzer(factory)
					.analyze());
			interpreter.setWriter(output);
			interpreter.setErrorWriter(error);
			interpreter.setReader(input);
			interpreter.execute();
		} catch (AnalysisException e) {
			throw new ScriptBasicException(e);
		} catch (ExecutionException e) {
			throw new ScriptBasicException(e);
		} catch (IOException e) {
			throw new ScriptBasicException(e);
		}
	}

	@Override
	public void eval(final String sourceCode) throws ScriptBasicException {
		eval(new StringReader(sourceCode), null, null);
	}

	@Override
	public void eval(final Reader reader) throws ScriptBasicException {
		eval(reader, null, null);
	}

	@Override
	public void eval(final File sourceFile) throws ScriptBasicException {
		try {
			eval(new FileReader(sourceFile), sourceFile.getAbsolutePath(), null);
		} catch (FileNotFoundException e) {
			throw new ScriptBasicException(e);
		}
	}

	@Override
	public void eval(String sourceFileName, String... path)
			throws ScriptBasicException {
		FileSourceProvider sourceProvider = new FileSourceProvider();
		BasicSourcePath sourcePath = new BasicSourcePath();
		for (String p : path) {
			sourcePath.add(p);
		}
		sourceProvider.setSourcePath(sourcePath);
		eval(null, sourceFileName, sourceProvider);
	}

	@Override
	public void eval(final String sourceFileName, final SourcePath path)
			throws ScriptBasicException {
		FileSourceProvider sourceProvider = new FileSourceProvider();
		sourceProvider.setSourcePath(path);
		eval(null, sourceFileName, sourceProvider);
	}

	@Override
	public void eval(final String sourceName, final SourceProvider provider)
			throws ScriptBasicException {
		eval(null, sourceName, provider);
	}

	public void setVariable(final String name, final Object value)
			throws ScriptBasicException {
		try {
			interpreter.getVariables().setVariable(name,
					RightValueUtility.createRightValue(value));
		} catch (ExecutionException e) {
			throw new ScriptBasicException(e);
		}
	}

	@Override
	public Object getVariable(final String name) throws ScriptBasicException {
		try {
			return interpreter.getVariable(name);
		} catch (ExecutionException e) {
			throw new ScriptBasicException(e);
		}
	}

	@Override
	public Iterable<String> getVariablesIterator() {
		return interpreter.getVariables().getGlobalMap().getVariableNameSet();
	}

	@Override
	public Object call(String subroutineName, Object... args)
			throws ScriptBasicException {
		try {
			return interpreter.call(subroutineName, args);
		} catch (ExecutionException e) {
			throw new ScriptBasicException(e);
		}
	}
}

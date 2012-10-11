package com.scriptbasic;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;

import com.scriptbasic.errors.BasicInterpreterInternalError;
import com.scriptbasic.executors.commands.CommandSub;
import com.scriptbasic.factories.FactoryFactory;
import com.scriptbasic.interfaces.AnalysisException;
import com.scriptbasic.interfaces.EngineApi;
import com.scriptbasic.interfaces.ExecutionException;
import com.scriptbasic.interfaces.ExtendedInterpreter;
import com.scriptbasic.interfaces.Factory;
import com.scriptbasic.interfaces.HierarchicalReader;
import com.scriptbasic.interfaces.LexicalAnalyzer;
import com.scriptbasic.interfaces.ScriptBasicException;
import com.scriptbasic.interfaces.Subroutine;
import com.scriptbasic.interfaces.SourcePath;
import com.scriptbasic.interfaces.SourceProvider;
import com.scriptbasic.readers.GenericHierarchicalReader;
import com.scriptbasic.readers.GenericReader;
import com.scriptbasic.sourceproviders.BasicSourcePath;
import com.scriptbasic.sourceproviders.FileSourceProvider;
import com.scriptbasic.utility.FactoryUtility;
import com.scriptbasic.utility.MethodRegisterUtility;
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

	private void loadHelper(final Reader reader, final String fileName,
			final SourceProvider sourceProvider) throws ScriptBasicException {
		try {
			final com.scriptbasic.interfaces.Reader sourceReader;
			if (reader == null && sourceProvider != null) {
				sourceReader = sourceProvider.get(fileName);
			} else {
				final GenericReader genericReader = new GenericReader();
				genericReader.set(reader);
				genericReader.setSourceProvider(sourceProvider);
				sourceReader = genericReader;
			}
			sourceReader.set((String) fileName);
			final LexicalAnalyzer lexicalAnalyzer = FactoryUtility
					.getLexicalAnalyzer(factory);
			final HierarchicalReader hReader = new GenericHierarchicalReader();
			hReader.include(sourceReader);
			lexicalAnalyzer.set(hReader);
			interpreter.setProgram(FactoryUtility.getSyntaxAnalyzer(factory)
					.analyze());
			interpreter.setWriter(output);
			interpreter.setErrorWriter(error);
			interpreter.setReader(input);
		} catch (final IOException e) {
			throw new ScriptBasicException(e);
		} catch (final AnalysisException e) {
			throw new ScriptBasicException(e);
		}
	}

	@Override
	public void execute() throws ScriptBasicException {
		try {
			interpreter.execute();
		} catch (final ExecutionException e) {
			throw new ScriptBasicException(e);
		}

	}

	@Override
	public void load(final String sourceCode) throws ScriptBasicException {
		loadHelper(new StringReader(sourceCode), null, null);
	}

	@Override
	public void eval(final String sourceCode) throws ScriptBasicException {
		load(sourceCode);
		execute();
	}

	@Override
	public void load(final Reader reader) throws ScriptBasicException {
		loadHelper(reader, null, null);
	}

	@Override
	public void eval(final Reader reader) throws ScriptBasicException {
		load(reader);
		execute();
	}

	@Override
	public void load(final File sourceFile) throws ScriptBasicException {
		try {
			loadHelper(new FileReader(sourceFile),
					sourceFile.getAbsolutePath(), null);
		} catch (final FileNotFoundException e) {
			throw new ScriptBasicException(e);
		}
	}

	@Override
	public void eval(final File sourceFile) throws ScriptBasicException {
		load(sourceFile);
		execute();
	}

	@Override
	public void load(final String sourceFileName, final String... path)
			throws ScriptBasicException {
		final FileSourceProvider sourceProvider = new FileSourceProvider();
		final BasicSourcePath sourcePath = new BasicSourcePath();
		for (final String p : path) {
			sourcePath.add(p);
		}
		sourceProvider.setSourcePath(sourcePath);
		loadHelper(null, sourceFileName, sourceProvider);
	}

	@Override
	public void eval(final String sourceFileName, final String... path)
			throws ScriptBasicException {
		load(sourceFileName, path);
		execute();
	}

	@Override
	public void load(final String sourceFileName, final SourcePath path)
			throws ScriptBasicException {
		final FileSourceProvider sourceProvider = new FileSourceProvider();
		sourceProvider.setSourcePath(path);
		loadHelper(null, sourceFileName, sourceProvider);
	}

	@Override
	public void eval(final String sourceFileName, final SourcePath path)
			throws ScriptBasicException {
		load(sourceFileName, path);
		execute();
	}

	@Override
	public void load(final String sourceName, final SourceProvider provider)
			throws ScriptBasicException {
		loadHelper(null, sourceName, provider);
	}

	@Override
	public void eval(final String sourceName, final SourceProvider provider)
			throws ScriptBasicException {
		load(sourceName, provider);
		execute();
	}

	public void setVariable(final String name, final Object value)
			throws ScriptBasicException {
		try {
			interpreter.getVariables().setVariable(name,
					RightValueUtility.createRightValue(value));
		} catch (final ExecutionException e) {
			throw new ScriptBasicException(e);
		}
	}

	@Override
	public Object getVariable(final String name) throws ScriptBasicException {
		try {
			return interpreter.getVariable(name);
		} catch (final ExecutionException e) {
			throw new ScriptBasicException(e);
		}
	}

	@Override
	public Iterable<String> getVariablesIterator() {
		return interpreter.getVariables().getGlobalMap().getVariableNameSet();
	}

	@Override
	public Object call(final String subroutineName, final Object... args)
			throws ScriptBasicException {
		try {
			return interpreter.call(subroutineName, args);
		} catch (final ExecutionException e) {
			throw new ScriptBasicException(e);
		}
	}

	@Override
	public Iterable<String> getSubroutineNames() {
		return interpreter.getProgram().getNamedCommandNames();
	}

	private boolean theMapHasToBeFilled = true;

	private void SubroutineDoesNotExistWTF(final Exception e) {
		throw new BasicInterpreterInternalError(
				"An already located subroutine does not exist", e);
	}

	@Override
	public Iterable<Subroutine> getSubroutines() {
		if (theMapHasToBeFilled) {
			for (final String s : getSubroutineNames()) {
				try {
					getSubroutine(s);
				} catch (final ScriptBasicException e) {
					SubroutineDoesNotExistWTF(e);
				}
			}
			theMapHasToBeFilled = false;
		}
		return subroutines.values();
	}

	private CommandSub getCommandSub(final String subroutineName)
			throws ScriptBasicException {
		final CommandSub commandSub = interpreter.getSubroutine(subroutineName);
		if (commandSub == null) {
			throw new ScriptBasicException("Sobroutine '" + subroutineName
					+ "' is not defined in the program");
		}
		return commandSub;
	}

	@Override
	public int getNumberOfArguments(final String subroutineName)
			throws ScriptBasicException {
		final CommandSub commandSub = getCommandSub(subroutineName);
		final int size;
		if (commandSub.getArguments() != null) {
			size = commandSub.getArguments().size();
		} else {
			size = 0;
		}
		return size;
	}

	private final Map<String, Subroutine> subroutines = new HashMap<String, Subroutine>();

	@Override
	public Subroutine getSubroutine(final String subroutineName)
			throws ScriptBasicException {
		if (subroutines.containsKey(subroutineName)) {
			return subroutines.get(subroutineName);
		}
		final CommandSub commandSub = getCommandSub(subroutineName);
		final Subroutine sub = new Sub(commandSub.getSubName());
		subroutines.put(subroutineName, sub);
		return sub;
	}

	public class Sub implements Subroutine {
		private final String name;

		Sub(final String n) {
			name = n;
		}

		@Override
		public int getNumberOfArguments() {
			try {
				return Engine.this.getNumberOfArguments(name);
			} catch (final ScriptBasicException e) {
				SubroutineDoesNotExistWTF(e);
				return 0;// will not get here
			}
		}

		@Override
		public String getName() {
			return name;
		}

		@Override
		public Object call(final Object... args) throws ScriptBasicException {
			return Engine.this.call(name, args);
		}

		@Override
		public Object call() throws ScriptBasicException {
			return call((Object[]) null);
		}
	}

	@Override
	public void registerExtension(final Class<?> klass)
			throws ScriptBasicException {
		MethodRegisterUtility.registerFunctions(klass, interpreter);
	}

}

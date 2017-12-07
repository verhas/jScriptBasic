package com.scriptbasic;

import com.scriptbasic.api.Configuration;
import com.scriptbasic.api.ScriptBasic;
import com.scriptbasic.api.ScriptBasicException;
import com.scriptbasic.api.Subroutine;
import com.scriptbasic.context.Context;
import com.scriptbasic.context.ContextBuilder;
import com.scriptbasic.errors.BasicInterpreterInternalError;
import com.scriptbasic.executors.commands.CommandSub;
import com.scriptbasic.interfaces.AnalysisException;
import com.scriptbasic.interfaces.SourcePath;
import com.scriptbasic.interfaces.SourceProvider;
import com.scriptbasic.interfaces.SourceReader;
import com.scriptbasic.sourceproviders.BasicSourcePath;
import com.scriptbasic.sourceproviders.FileSourceProvider;
import com.scriptbasic.spi.InterpreterHook;
import com.scriptbasic.utility.RightValueUtility;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class Engine implements ScriptBasic {

    private final Map<String, Subroutine> subroutines = new HashMap<>();
    private Reader input;
    private Writer output;
    private Writer error;
    private boolean theMapHasToBeFilled = true;
    private Context ctx;

    public Engine() {
    }


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
    public Writer getErrorOutput() {
        return error;
    }

    @Override
    public void setErrorOutput(final Writer error) {
        this.error = error;
    }

    private void loadHelper(final Reader reader) throws ScriptBasicException {
        loadHelper(reader, null);
    }

    private void loadHelper(final String fileName, final SourceProvider sourceProvider) throws ScriptBasicException {
        final SourceReader sourceReader;
        try {
            sourceReader = sourceProvider.get(fileName);
            loadHelper(sourceReader);
        } catch (final IOException e) {
            throw new ScriptBasicException(e);
        }
    }

    private void loadHelper(final Reader reader, final String fileName) throws ScriptBasicException {
        try {
            ctx = ContextBuilder.from(ctx, reader, input, output, error);
            ctx.interpreter.setProgram(ctx.syntaxAnalyzer.analyze());
        } catch (final AnalysisException e) {
            throw new ScriptBasicException(e);
        }
    }

    private void loadHelper(final SourceReader sourceReader) throws ScriptBasicException {
        try {
            ctx = ContextBuilder.from(ctx, sourceReader, input, output, error);
            ctx.interpreter.setProgram(ctx.syntaxAnalyzer.analyze());
        } catch (final AnalysisException e) {
            throw new ScriptBasicException(e);
        }
    }

    @Override
    public void execute() throws ScriptBasicException {
        assertCtxInitialized();
        ctx.interpreter.execute();
    }

    private void assertCtxInitialized() throws ScriptBasicException {
        if (ctx == null) {
            throw new ScriptBasicException("Interpreter was not properly initialized.");
        }
    }

    @Override
    public void load(final String sourceCode) throws ScriptBasicException {
        loadHelper(new StringReader(sourceCode));
    }

    @Override
    public void eval(final String sourceCode) throws ScriptBasicException {
        load(sourceCode);
        execute();
    }

    @Override
    public void load(final Reader reader) throws ScriptBasicException {
        loadHelper(reader);
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
                    sourceFile.getAbsolutePath());
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
        loadHelper(sourceFileName, sourceProvider);
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
        loadHelper(sourceFileName, sourceProvider);
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
        loadHelper(sourceName, provider);
    }

    @Override
    public void eval(final String sourceName, final SourceProvider provider)
            throws ScriptBasicException {
        load(sourceName, provider);
        execute();
    }

    public void setVariable(final String name, final Object value)
            throws ScriptBasicException {
        ctx = ContextBuilder.from(ctx);
        ctx.interpreter.getVariables().setVariable(name,
                RightValueUtility.createRightValue(value));
    }

    @Override
    public Object getVariable(final String name) throws ScriptBasicException {
        assertCtxInitialized();
        return ctx.interpreter.getVariable(name);
    }

    @Override
    public Iterable<String> getVariablesIterator() throws ScriptBasicException {
        assertCtxInitialized();
        return ctx.interpreter.getVariables().getGlobalMap().getVariableNameSet();
    }


    private void SNAFU_SubroutineDoesNotExist(final Exception e) {
        throw new BasicInterpreterInternalError(
                "An already located subroutine does not exist", e);
    }

    @Override
    public Iterable<Subroutine> getSubroutines() throws ScriptBasicException {
        if (theMapHasToBeFilled) {
            assertCtxInitialized();
            for (final String s : ctx.interpreter.getProgram().getNamedCommandNames()) {
                try {
                    getSubroutine(s);
                } catch (final ScriptBasicException e) {
                    SNAFU_SubroutineDoesNotExist(e);
                }
            }
            theMapHasToBeFilled = false;
        }
        return subroutines.values();
    }

    private CommandSub getCommandSub(final String subroutineName)
            throws ScriptBasicException {
        assertCtxInitialized();
        final CommandSub commandSub = ctx.interpreter.getSubroutine(subroutineName);
        if (commandSub == null) {
            throw new ScriptBasicException("Sobroutine '" + subroutineName
                    + "' is not defined in the program");
        }
        return commandSub;
    }

    private int getNumberOfArguments(final String subroutineName)
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

    public void registerFunction(final String alias,
                                 final Class<?> klass,
                                 final String methodName,
                                 final Class<?>... argumentTypes) throws ScriptBasicException {
        ctx = ContextBuilder.from(ctx);
        ctx.interpreter.registerJavaMethod(alias, klass, methodName, argumentTypes);
    }

    @Override
    public void registerExtension(final Class<?> klass)
            throws ScriptBasicException {
        ctx = ContextBuilder.from(ctx);
        ctx.interpreter.registerFunctions(klass);
    }

    @Override
    public void registerHook(InterpreterHook hook) {
        ctx = ContextBuilder.from(ctx);
        ctx.interpreter.registerHook(hook);
    }

    @Override
    public Configuration getConfiguration() {
        ctx = ContextBuilder.from(ctx);
        return ctx.configuration;
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
                SNAFU_SubroutineDoesNotExist(e);
                return 0;// will not get here
            }
        }

        @Override
        public String getName() {
            return name;
        }

        @Override
        public Object call(final Object... args) throws ScriptBasicException {
            assertCtxInitialized();
            return ctx.interpreter.call(name, args);
        }

        @Override
        public Object call() throws ScriptBasicException {
            return call((Object[]) null);
        }
    }

}

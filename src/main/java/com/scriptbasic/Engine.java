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
import com.scriptbasic.readers.SourcePath;
import com.scriptbasic.readers.SourceProvider;
import com.scriptbasic.readers.SourceReader;
import com.scriptbasic.sourceproviders.BasicSourcePath;
import com.scriptbasic.sourceproviders.FileSourceProvider;
import com.scriptbasic.spi.InterpreterHook;
import com.scriptbasic.utility.RightValueUtility;

import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

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
    public ScriptBasic execute() throws ScriptBasicException {
        assertCtxInitialized();
        ctx.interpreter.execute();
        return this;
    }

    private void assertCtxInitialized() throws ScriptBasicException {
        if (ctx == null) {
            throw new ScriptBasicException("Interpreter was not properly initialized.");
        }
    }

    @Override
    public ScriptBasic load(final String sourceCode) throws ScriptBasicException {
        loadHelper(new StringReader(sourceCode));
        return this;
    }

    @Override
    public ScriptBasic eval(final String sourceCode) throws ScriptBasicException {
        load(sourceCode);
        execute();
        return this;

    }

    @Override
    public ScriptBasic load(final Reader reader) throws ScriptBasicException {
        loadHelper(reader);
        return this;

    }

    @Override
    public ScriptBasic eval(final Reader reader) throws ScriptBasicException {
        load(reader);
        execute();
        return this;

    }

    @Override
    public ScriptBasic load(final File sourceFile) throws ScriptBasicException {
        try {
            loadHelper(new FileReader(sourceFile),
                    sourceFile.getAbsolutePath());
        } catch (final FileNotFoundException e) {
            throw new ScriptBasicException(e);
        }
        return this;

    }

    @Override
    public ScriptBasic eval(final File sourceFile) throws ScriptBasicException {
        load(sourceFile);
        execute();
        return this;

    }

    @Override
    public ScriptBasic load(final String sourceFileName, final String... path)
            throws ScriptBasicException {
        final FileSourceProvider sourceProvider = new FileSourceProvider();
        final BasicSourcePath sourcePath = new BasicSourcePath();
        for (final String p : path) {
            sourcePath.add(p);
        }
        sourceProvider.setSourcePath(sourcePath);
        loadHelper(sourceFileName, sourceProvider);
        return this;

    }

    @Override
    public ScriptBasic eval(final String sourceFileName, final String... path)
            throws ScriptBasicException {
        load(sourceFileName, path);
        execute();
        return this;

    }

    @Override
    public ScriptBasic load(final String sourceFileName, final SourcePath path)
            throws ScriptBasicException {
        final FileSourceProvider sourceProvider = new FileSourceProvider();
        sourceProvider.setSourcePath(path);
        loadHelper(sourceFileName, sourceProvider);
        return this;

    }

    @Override
    public ScriptBasic eval(final String sourceFileName, final SourcePath path)
            throws ScriptBasicException {
        load(sourceFileName, path);
        execute();
        return this;

    }

    @Override
    public ScriptBasic load(final String sourceName, final SourceProvider provider)
            throws ScriptBasicException {
        loadHelper(sourceName, provider);
        return this;

    }

    @Override
    public ScriptBasic eval(final String sourceName, final SourceProvider provider)
            throws ScriptBasicException {
        load(sourceName, provider);
        execute();
        return this;

    }

    public void setVariable(final String name, final Object value)
            throws ScriptBasicException {
        ctx = ContextBuilder.from(ctx);
        ctx.interpreter.getVariables().setVariable(name,
                RightValueUtility.createRightValue(value));
    }

    @SuppressWarnings("removal")
    @Override
    public Object getVariable(final String name) throws ScriptBasicException {
        return variable(Object.class, name);
    }

    @Override
    public <T> T variable(final Class<T> type, final String name) throws ScriptBasicException {
        assertCtxInitialized();
        final var result = ctx.interpreter.getVariable(name);
        if (result == null || type.isAssignableFrom(result.getClass())) {
            return (T) result;
        }
        throw new ScriptBasicException("Fetching the variable '"
                + name
                + "' casting to type "
                + type.getName()
                + " failed from "
                + result.getClass().getName());
    }

    @SuppressWarnings("removal")
    @Override
    public Iterable<String> getVariablesIterator() throws ScriptBasicException {
        return variables();
    }

    @Override
    public Iterable<String> variables() throws ScriptBasicException {
        assertCtxInitialized();
        return ctx.interpreter.getVariables().getGlobalMap().getVariableNameSet();
    }


    private void SNAFU_SubroutineDoesNotExist(final Exception e) {
        throw new BasicInterpreterInternalError(
                "An already located subroutine does not exist", e);
    }

    @Override
    public Iterable<Subroutine> subroutines() throws ScriptBasicException {
        if (theMapHasToBeFilled) {
            assertCtxInitialized();
            for (final String s : ctx.interpreter.getProgram().getNamedCommandNames()) {
                try {
                    subroutine(Object.class, s);
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
    public <R> Subroutine<R> subroutine(final String name)
            throws ScriptBasicException {
        return subroutine(null, name);
    }

    @Override
    public <R> Subroutine<R> subroutine(final Class<R> type, final String name)
            throws ScriptBasicException {
        if (subroutines.containsKey(name)) {
            return subroutines.get(name);
        }
        final CommandSub commandSub = getCommandSub(name);
        final Subroutine sub = new Sub(type, commandSub.getSubName());
        subroutines.put(name, sub);
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
    public ScriptBasic registerExtension(final Class<?> klass)
            throws ScriptBasicException {
        ctx = ContextBuilder.from(ctx);
        ctx.interpreter.registerFunctions(klass);
        return this;

    }

    @Override
    public ScriptBasic registerHook(InterpreterHook hook) {
        ctx = ContextBuilder.from(ctx);
        ctx.interpreter.registerHook(hook);
        return this;

    }

    @Override
    public Configuration getConfiguration() {
        ctx = ContextBuilder.from(ctx);
        return ctx.configuration;
    }

    public class Sub<R> implements Subroutine<R> {
        private final String name;
        private final Class<R> type;

        Sub(final Class<R> t, final String n) {
            name = n;
            type = Objects.requireNonNullElseGet(t, () -> (Class<R>) Object.class);
        }

        @Override
        public int numberOfArguments() {
            try {
                return Engine.this.getNumberOfArguments(name);
            } catch (final ScriptBasicException e) {
                SNAFU_SubroutineDoesNotExist(e);
                return 0;// will not get here
            }
        }

        @Override
        public String name() {
            return name;
        }

        @Override
        public R call(final Object... args) throws ScriptBasicException {
            assertCtxInitialized();
            final var result = ctx.interpreter.call(name, args);
            if (type == Void.class && result != null) {
                throw new ScriptBasicException("Subroutine '"
                        + name
                        + "' expected to be Void but returns value of type "
                        + type.getName());
            }
            if (result == null || type.isAssignableFrom(result.getClass())) {
                return (R) result;
            }
            throw new ScriptBasicException("Fetching the return value of subroutine '"
                    + name
                    + "' casting to type "
                    + type.getName()
                    + " failed from "
                    + result.getClass().getName());
        }

        @Override
        public R call() throws ScriptBasicException {
            return call((Object[]) null);
        }
    }

}

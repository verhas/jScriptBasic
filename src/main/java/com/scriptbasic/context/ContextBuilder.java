package com.scriptbasic.context;

import com.scriptbasic.configuration.BasicConfiguration;
import com.scriptbasic.executors.BasicInterpreter;
import com.scriptbasic.interfaces.AnalysisException;
import com.scriptbasic.lexer.elements.ScriptBasicLexicalAnalyzer;
import com.scriptbasic.readers.GenericHierarchicalSourceReader;
import com.scriptbasic.readers.GenericSourceReader;
import com.scriptbasic.readers.SourceReader;
import com.scriptbasic.syntax.BasicSyntaxAnalyzer;
import com.scriptbasic.syntax.GenericNestedStructureHouseKeeper;
import com.scriptbasic.syntax.commands.BasicCommandFactory;
import com.scriptbasic.syntax.expression.BasicExpressionAnalyzer;
import com.scriptbasic.syntax.expression.BasicExpressionListAnalyzer;
import com.scriptbasic.syntax.expression.BasicTagAnalyzer;
import com.scriptbasic.syntax.leftvalue.BasicLeftValueAnalyzer;
import com.scriptbasic.syntax.leftvalue.BasicSimpleLeftValueAnalyzer;
import com.scriptbasic.syntax.leftvalue.BasicSimpleLeftValueListAnalyzer;

import java.io.Reader;
import java.io.StringReader;
import java.io.Writer;
import java.util.Objects;

public class ContextBuilder {

    public static Context newContext() {
        final var ctx = new Context();
        ctx.interpreter = new BasicInterpreter(ctx);
        return ctx;
    }

    public static Context from(final Context existingCtx) {
        final var ctx = Objects.requireNonNullElseGet(existingCtx, Context::new);
        if (ctx.interpreter == null) {
            ctx.interpreter = new BasicInterpreter(ctx);
        }
        if (ctx.configuration == null) {
            ctx.configuration = new BasicConfiguration();
        }
        return ctx;
    }

    public static Context from(final Reader reader, final Reader input, final Writer output, final Writer error) {
        return from(null, reader, input, output, error);
    }

    public static Context from(final Context existing, final Reader reader, final Reader input, final Writer output, final Writer error) {
        final var ctx = from(existing, reader);
        ctx.interpreter.setInput(input);
        ctx.interpreter.setOutput(output);
        ctx.interpreter.setErrorOutput(error);
        return ctx;
    }

    public static Context from(final String string) throws AnalysisException {
        return from(null, string);
    }

    private static Context from(final Context existing, final String string) {
        return from(existing, new StringReader(string));
    }

    public static Context from(final Reader reader) {
        return from(null, reader);
    }

    public static Context from(final Context existing, final Reader reader) {
        final var sourceReader = new GenericSourceReader(reader, null, null);
        final var hReader = new GenericHierarchicalSourceReader(sourceReader);
        return from(existing, hReader);
    }

    public static Context from(final SourceReader sourceReader, final Reader input, final Writer output, final Writer error) throws AnalysisException {
        return from(null, sourceReader, input, output, error);
    }

    public static Context from(final Context existing, final SourceReader sourceReader, final Reader input, final Writer output, final Writer error) {
        final var ctx = from(existing, sourceReader);
        ctx.interpreter.setInput(input);
        ctx.interpreter.setOutput(output);
        ctx.interpreter.setErrorOutput(error);
        return ctx;
    }

    public static Context from(final SourceReader reader) {
        return from(null, reader);
    }

    private static Context from(final Context existing, final SourceReader reader) {
        final var ctx = from(existing);
        createReusableComponents(ctx);
        createReaderDependentComponents(reader, ctx);
        return ctx;
    }

    private static void createReaderDependentComponents(final SourceReader reader, final Context ctx) {
        ctx.lexicalAnalyzer = new ScriptBasicLexicalAnalyzer(reader);
        ctx.nestedStructureHouseKeeper = new GenericNestedStructureHouseKeeper(ctx.lexicalAnalyzer);
        final var commandFactory = new BasicCommandFactory(ctx);
        ctx.syntaxAnalyzer = new BasicSyntaxAnalyzer(ctx.lexicalAnalyzer, commandFactory,
                                                     ctx.nestedStructureHouseKeeper);
    }

    private static void createReusableComponents(final Context ctx) {
        if (ctx.configuration == null) {
            ctx.configuration = new BasicConfiguration();
        }
        if (ctx.simpleLeftValueListAnalyzer == null) {
            ctx.simpleLeftValueListAnalyzer = new BasicSimpleLeftValueListAnalyzer(ctx);
        }
        if (ctx.simpleLeftValueAnalyzer == null) {
            ctx.simpleLeftValueAnalyzer = new BasicSimpleLeftValueAnalyzer(ctx);
        }
        if (ctx.expressionAnalyzer == null) {
            ctx.expressionAnalyzer = new BasicExpressionAnalyzer(ctx);
        }
        if (ctx.expressionListAnalyzer == null) {
            ctx.expressionListAnalyzer = new BasicExpressionListAnalyzer(ctx);
        }
        if (ctx.tagAnalyzer == null) {
            ctx.tagAnalyzer = new BasicTagAnalyzer(ctx);
        }
        if (ctx.leftValueAnalyzer == null) {
            ctx.leftValueAnalyzer = new BasicLeftValueAnalyzer(ctx);
        }
    }
}

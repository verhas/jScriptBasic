package com.scriptbasic.factories;

import com.scriptbasic.configuration.BasicConfiguration;
import com.scriptbasic.executors.BasicExtendedInterpreter;
import com.scriptbasic.interfaces.AnalysisException;
import com.scriptbasic.interfaces.CommandFactory;
import com.scriptbasic.interfaces.HierarchicalSourceReader;
import com.scriptbasic.interfaces.SourceReader;
import com.scriptbasic.lexer.elements.ScriptBasicLexicalAnalyzer;
import com.scriptbasic.readers.GenericHierarchicalSourceReader;
import com.scriptbasic.readers.GenericSourceReader;
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

public class ContextBuilder {

    public static Context newContext() {
        final Context ctx = new Context();
        ctx.interpreter = new BasicExtendedInterpreter(ctx);
        return ctx;
    }

    public static Context from(Context existingCtx) {
        final Context ctx;
        if (existingCtx != null) {
            ctx = existingCtx;
        } else {
            ctx = new Context();
        }
        if (ctx.interpreter == null) {
            ctx.interpreter = new BasicExtendedInterpreter(ctx);
        }
        return ctx;
    }

    public static Context from(Reader reader, Reader input, Writer output, Writer error) throws AnalysisException {
        return from(null, reader, input, output, error);
    }

    public static Context from(Context existing, Reader reader, Reader input, Writer output, Writer error) throws AnalysisException {
        Context ctx = from(existing, reader);
        ctx.interpreter.setInput(input);
        ctx.interpreter.setOutput(output);
        ctx.interpreter.setError(error);
        return ctx;
    }

    public static Context from(String string) throws AnalysisException {
        return from(null, string);
    }

    private static Context from(Context existing, String string) throws AnalysisException {
        return from(existing, new StringReader(string));
    }

    public static Context from(Reader reader) throws AnalysisException {
        return from(null, reader);
    }

    private static Context from(Context existing, Reader reader) throws AnalysisException {
        final GenericSourceReader sourceReader = new GenericSourceReader(reader, null, null);
        final HierarchicalSourceReader hReader = new GenericHierarchicalSourceReader(sourceReader);
        return from(existing, hReader);
    }

    public static Context from(SourceReader sourceReader, Reader input, Writer output, Writer error) throws AnalysisException {
        return from(null, sourceReader, input, output, error);
    }

    public static Context from(Context existing, SourceReader sourceReader, Reader input, Writer output, Writer error) throws AnalysisException {
        Context ctx = from(existing, sourceReader);
        ctx.interpreter.setInput(input);
        ctx.interpreter.setOutput(output);
        ctx.interpreter.setError(error);
        return ctx;
    }

    public static Context from(SourceReader reader) throws AnalysisException {
        return from(null, reader);
    }

    private static Context from(Context existing, SourceReader reader) throws AnalysisException {
        Context ctx = from(existing);
        createReusableComponents(ctx);
        createReaderDependentComponents(reader, ctx);
        return ctx;
    }

    private static void createReaderDependentComponents(SourceReader reader, Context ctx) {
        ctx.lexicalAnalyzer = new ScriptBasicLexicalAnalyzer(reader);
        ctx.nestedStructureHouseKeeper = new GenericNestedStructureHouseKeeper(ctx.lexicalAnalyzer);
        final CommandFactory commandFactory = new BasicCommandFactory(ctx);
        ctx.syntaxAnalyzer = new BasicSyntaxAnalyzer(ctx.lexicalAnalyzer, commandFactory);
    }

    private static void createReusableComponents(Context ctx) {
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

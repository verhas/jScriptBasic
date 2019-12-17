package com.scriptbasic.syntax.commands;

import com.scriptbasic.context.Context;
import com.scriptbasic.exceptions.CommandFactoryException;
import com.scriptbasic.interfaces.AnalysisException;
import com.scriptbasic.interfaces.CommandAnalyzer;
import com.scriptbasic.interfaces.CommandFactory;
import com.scriptbasic.log.Logger;
import com.scriptbasic.log.LoggerFactory;
import com.scriptbasic.spi.Command;

import java.util.List;
import java.util.Map;

public final class BasicCommandFactory implements CommandFactory {
    private static final Logger LOG = LoggerFactory
            .getLogger();
    private final Context ctx;
    private final CommandAnalyzerDSL dslAnalyzer;
    private final Map<String, CommandAnalyzer> classMap;
    private final List<CommandAnalyzer> classList;

    /**
     * This implementation of the interface (btw: the only one in ScriptBasic) creates a command
     * from a list of symbols. First it checks if the first symbol of the command is a keyword and
     * appears in the {@code classMap} as a key. If yes, for example the
     *
     * @param ctx the context of the interpreter
     */
    public BasicCommandFactory(final Context ctx) {
        this.ctx = ctx;
        dslAnalyzer = new CommandAnalyzerDSL(ctx);
        classMap = Map.ofEntries(
                Map.entry("while", new CommandAnalyzerWhile(ctx)),
                Map.entry("wend", new CommandAnalyzerWend(ctx)),
                Map.entry("if", new CommandAnalyzerIf(ctx)),
                Map.entry("else", new CommandAnalyzerElse(ctx)),
                Map.entry("elseif", new CommandAnalyzerElseIf(ctx)),
                Map.entry("endif", new CommandAnalyzerEndIf(ctx)),
                Map.entry("use", new CommandAnalyzerUse(ctx)),
                Map.entry("method", new CommandAnalyzerMethod(ctx)),
                Map.entry("sub", new CommandAnalyzerSub(ctx)),
                Map.entry("endsub", new CommandAnalyzerEndSub(ctx)),
                Map.entry("return", new CommandAnalyzerReturn(ctx)),
                Map.entry("print", new CommandAnalyzerPrint(ctx)),
                Map.entry("local", new CommandAnalyzerLocal(ctx)),
                Map.entry("global", new CommandAnalyzerGlobal(ctx)),
                Map.entry("call", new CommandAnalyzerCall(ctx)),
                Map.entry("let", new CommandAnalyzerLet(ctx)),
                Map.entry("for", new CommandAnalyzerFor(ctx)),
                Map.entry("next", new CommandAnalyzerNext(ctx)),
                Map.entry("sentence", dslAnalyzer));
        classList = List.of(
                new CommandAnalyzerLet(ctx),
                new CommandAnalyzerCall(ctx),
                dslAnalyzer);
    }

    @Override
    public Command create(final String commandKeyword) throws AnalysisException {
        if (commandKeyword == null) {
            return create();
        } else {
            return createFromStartingSymbol(commandKeyword);
        }
    }

    private Command create() throws AnalysisException {
        for (final CommandAnalyzer commandAnalyzer : classList) {
            try {
                LOG.debug("trying to analyze the line using {}", commandAnalyzer.getClass());
                final var command = commandAnalyzer.analyze();
                if (command != null) {
                    return command;
                }
            } catch (final AnalysisException e) {
                LOG.debug("Tried but not analyze the line using "
                        + commandAnalyzer.getClass(), e);
            }
            ctx.lexicalAnalyzer.resetLine();
        }
        LOG.debug("None of the analyzers could analyze the line");
        throw new CommandFactoryException("The line could not be analyzed");
    }

    private Command createFromStartingSymbol(final String commandKeyword)
            throws AnalysisException {
        final var lowerCaseCommandKeyword = commandKeyword.toLowerCase();
        LOG.debug("Creating command starting with the keyword '{}'",
                lowerCaseCommandKeyword);
        if (classMap.containsKey(lowerCaseCommandKeyword)) {
            try {
                return classMap.get(lowerCaseCommandKeyword).analyze();
            } catch (final AnalysisException originalException) {
                try {
                    return dslAnalyzer.analyze();
                } catch (final AnalysisException ignored) {
                    throw originalException;
                }
            }
        }
        return dslAnalyzer.analyze();
    }

}

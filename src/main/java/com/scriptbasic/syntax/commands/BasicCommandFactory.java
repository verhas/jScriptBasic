package com.scriptbasic.syntax.commands;

import com.scriptbasic.context.Context;
import com.scriptbasic.exceptions.CommandFactoryException;
import com.scriptbasic.interfaces.AnalysisException;
import com.scriptbasic.interfaces.CommandAnalyzer;
import com.scriptbasic.interfaces.CommandFactory;
import com.scriptbasic.interfaces.ScriptBasicKeyWords;
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
                Map.entry(ScriptBasicKeyWords.KEYWORD_WHILE, new CommandAnalyzerWhile(ctx)),
                Map.entry(ScriptBasicKeyWords.KEYWORD_WEND, new CommandAnalyzerWend(ctx)),
                Map.entry(ScriptBasicKeyWords.KEYWORD_IF, new CommandAnalyzerIf(ctx)),
                Map.entry(ScriptBasicKeyWords.KEYWORD_ELSE, new CommandAnalyzerElse(ctx)),
                Map.entry(ScriptBasicKeyWords.KEYWORD_ELSEIF, new CommandAnalyzerElseIf(ctx)),
                Map.entry(ScriptBasicKeyWords.KEYWORD_ENDIF, new CommandAnalyzerEndIf(ctx)),
                Map.entry(ScriptBasicKeyWords.KEYWORD_USE, new CommandAnalyzerUse(ctx)),
                Map.entry(ScriptBasicKeyWords.KEYWORD_METHOD, new CommandAnalyzerMethod(ctx)),
                Map.entry(ScriptBasicKeyWords.KEYWORD_SUB, new CommandAnalyzerSub(ctx)),
                Map.entry(ScriptBasicKeyWords.KEYWORD_ENDSUB, new CommandAnalyzerEndSub(ctx)),
                Map.entry(ScriptBasicKeyWords.KEYWORD_RETURN, new CommandAnalyzerReturn(ctx)),
                Map.entry(ScriptBasicKeyWords.KEYWORD_PRINT, new CommandAnalyzerPrint(ctx)),
                Map.entry(ScriptBasicKeyWords.KEYWORD_LOCAL, new CommandAnalyzerLocal(ctx)),
                Map.entry(ScriptBasicKeyWords.KEYWORD_GLOBAL, new CommandAnalyzerGlobal(ctx)),
                Map.entry(ScriptBasicKeyWords.KEYWORD_CALL, new CommandAnalyzerCall(ctx)),
                Map.entry(ScriptBasicKeyWords.KEYWORD_LET, new CommandAnalyzerLet(ctx)),
                Map.entry(ScriptBasicKeyWords.KEYWORD_FOR, new CommandAnalyzerFor(ctx)),
                Map.entry(ScriptBasicKeyWords.KEYWORD_NEXT, new CommandAnalyzerNext(ctx)),
                Map.entry(ScriptBasicKeyWords.KEYWORD_SELECT, new CommandAnalyzerSelect(ctx)),
                Map.entry(ScriptBasicKeyWords.KEYWORD_END, new CommandAnalyzerEnd(ctx)),
                Map.entry(ScriptBasicKeyWords.KEYWORD_CASE, new CommandAnalyzerCase(ctx)),
                Map.entry(ScriptBasicKeyWords.KEYWORD_SENTENCE, dslAnalyzer));
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

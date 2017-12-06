package com.scriptbasic.syntax.commands;

import com.scriptbasic.exceptions.CommandFactoryException;
import com.scriptbasic.exceptions.KeywordNotImplementedException;
import com.scriptbasic.context.Context;
import com.scriptbasic.interfaces.AnalysisException;
import com.scriptbasic.interfaces.Command;
import com.scriptbasic.interfaces.CommandAnalyzer;
import com.scriptbasic.interfaces.CommandFactory;
import com.scriptbasic.log.Logger;
import com.scriptbasic.log.LoggerFactory;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public final class BasicCommandFactory implements CommandFactory {
    private static final Logger LOG = LoggerFactory
            .getLogger();
    private final Context ctx;
    private Map<String, CommandAnalyzer> classMap = new HashMap<>();
    private List<CommandAnalyzer> classList = new LinkedList<>();

    public BasicCommandFactory(final Context ctx) {
        this.ctx = ctx;
        registerCommandAnalyzer("while", new CommandAnalyzerWhile(ctx));
        registerCommandAnalyzer("wend", new CommandAnalyzerWend(ctx));
        registerCommandAnalyzer("if", new CommandAnalyzerIf(ctx));
        registerCommandAnalyzer("else", new CommandAnalyzerElse(ctx));
        registerCommandAnalyzer("elseif", new CommandAnalyzerElseIf(ctx));
        registerCommandAnalyzer("endif", new CommandAnalyzerEndIf(ctx));
        registerCommandAnalyzer("use", new CommandAnalyzerUse(ctx));
        registerCommandAnalyzer("method", new CommandAnalyzerMethod(ctx));
        registerCommandAnalyzer("sub", new CommandAnalyzerSub(ctx));
        registerCommandAnalyzer("endsub", new CommandAnalyzerEndSub(ctx));
        registerCommandAnalyzer("return", new CommandAnalyzerReturn(ctx));
        registerCommandAnalyzer("print", new CommandAnalyzerPrint(ctx));
        registerCommandAnalyzer("local", new CommandAnalyzerLocal(ctx));
        registerCommandAnalyzer("global", new CommandAnalyzerGlobal(ctx));
        registerCommandAnalyzer("call", new CommandAnalyzerCall(ctx));
        registerCommandAnalyzer("let", new CommandAnalyzerLet(ctx));
        registerCommandAnalyzer("for", new CommandAnalyzerFor(ctx));
        registerCommandAnalyzer("next", new CommandAnalyzerNext(ctx));
        //
        registerCommandAnalyzer(new CommandAnalyzerLet(ctx));
        registerCommandAnalyzer(new CommandAnalyzerCall(ctx));
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.scriptbasic.interfaces.CommandFactory#registerCommandAnalyzer(java
     * .lang.String, com.scriptbasic.interfaces.CommandAnalyzer)
     */
    @Override
    public void registerCommandAnalyzer(final String keyword, final CommandAnalyzer analyzer) {
        LOG.info("Registering command {}", keyword);
        if (keyword == null) {
            classList.add(analyzer);
        } else {
            classMap.put(keyword, analyzer);
        }
    }

    private void registerCommandAnalyzer(final CommandAnalyzer analyzer) {
        registerCommandAnalyzer(null, analyzer);
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
                LOG.info("trying to analyze the line using {}", commandAnalyzer.getClass());
                final Command command = commandAnalyzer.analyze();
                if (command != null) {
                    return command;
                }
            } catch (final AnalysisException e) {
                LOG.info("Tried but not analyze the line using "
                        + commandAnalyzer.getClass(), e);
            }
            ctx.lexicalAnalyzer.resetLine();
        }
        LOG.info("None of the analyzers could analyze the line");
        throw new CommandFactoryException("The line could not be analyzed");
    }

    private Command createFromStartingSymbol(final String commandKeyword)
            throws AnalysisException {
        final String lowerCaseCommandKeyword = commandKeyword.toLowerCase();
        LOG.debug("Creating command starting with the keyword '{}'",
                lowerCaseCommandKeyword);
        if (!classMap.containsKey(lowerCaseCommandKeyword)) {
            throw new KeywordNotImplementedException(commandKeyword);
        }

        final CommandAnalyzer commandAnalyzer = classMap
                .get(lowerCaseCommandKeyword);
        return commandAnalyzer.analyze();
    }

}

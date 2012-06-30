package com.scriptbasic.syntax.commandanalyzers.commands;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.scriptbasic.errors.BasicInterpreterInternalError;
import com.scriptbasic.exceptions.CommandFactoryException;
import com.scriptbasic.exceptions.KeywordNotImplementedException;
import com.scriptbasic.interfaces.AnalysisException;
import com.scriptbasic.interfaces.Command;
import com.scriptbasic.interfaces.CommandAnalyzer;
import com.scriptbasic.interfaces.CommandFactory;
import com.scriptbasic.interfaces.Factory;
import com.scriptbasic.syntax.commandanalyzers.AbstractCommandAnalyzer;

public final class BasicCommandFactory implements CommandFactory {
    private static final Logger LOG = LoggerFactory
            .getLogger(BasicCommandFactory.class);
    private Factory factory;

    public Factory getFactory() {
        return factory;
    }

    @Override
    public void setFactory(Factory factory) {
        this.factory = factory;
    }

    private Map<String, CommandAnalyzer> classMap = new HashMap<>();
    private List<CommandAnalyzer> classList = new LinkedList<>();

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.scriptbasic.interfaces.CommandFactory#registerCommandAnalyzer(java
     * .lang.String, com.scriptbasic.interfaces.CommandAnalyzer)
     */
    @Override
    public void registerCommandAnalyzer(String keyword, CommandAnalyzer analyzer) {
        LOG.info("Registering command {}", keyword);
        if (keyword == null) {
            classList.add(analyzer);
        } else {
            classMap.put(keyword, analyzer);
        }
        if (analyzer instanceof AbstractCommandAnalyzer) {
            if (getFactory() == null) {
                throw new BasicInterpreterInternalError(
                        "BasicCommandFactory's factory is null, not initialized yet");
            }
            ((AbstractCommandAnalyzer) analyzer).setFactory(getFactory());
        }
    }

    private void registerCommandAnalyzer(CommandAnalyzer analyzer) {
        registerCommandAnalyzer(null, analyzer);
    }

    private BasicCommandFactory(Factory factory) {
        this.factory = factory;
        registerCommandAnalyzer("while", new CommandAnalyzerWhile());
        registerCommandAnalyzer("wend", new CommandAnalyzerWend());
        registerCommandAnalyzer("if", new CommandAnalyzerIf());
        registerCommandAnalyzer("else", new CommandAnalyzerElse());
        registerCommandAnalyzer("endif", new CommandAnalyzerEndIf());
        registerCommandAnalyzer("use", new CommandAnalyzerUse());
        registerCommandAnalyzer("method", new CommandAnalyzerMethod());

        registerCommandAnalyzer(new CommandAnalyzerLet());
        registerCommandAnalyzer(new CommandAnalyzerCall());
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
                LOG.info("trying to analyze the line using {}",
                        commandAnalyzer.getClass());
                final Command command = commandAnalyzer.analyze();
                if (command != null) {
                    return command;
                }
            } catch (AnalysisException e) {
                LOG.info("Tried but not analyze the line using "
                        + commandAnalyzer.getClass(), e);
            }
        }
        LOG.info("None of the analyzers could analyze the line");
        throw new CommandFactoryException("The line could not be analyzed");
    }

    private Command createFromStartingSymbol(final String commandKeyword)
            throws AnalysisException {
        final String loverCaseCommandKeyword = commandKeyword.toLowerCase();
        if (!classMap.containsKey(loverCaseCommandKeyword)) {
            throw new KeywordNotImplementedException(commandKeyword);
        }

        final CommandAnalyzer commandAnalyzer = classMap
                .get(loverCaseCommandKeyword);
        return commandAnalyzer.analyze();
    }

}

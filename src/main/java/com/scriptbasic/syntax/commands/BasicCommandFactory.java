package com.scriptbasic.syntax.commands;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.scriptbasic.errors.BasicInterpreterInternalError;
import com.scriptbasic.exceptions.CommandFactoryException;
import com.scriptbasic.exceptions.KeywordNotImplementedException;
import com.scriptbasic.interfaces.AnalysisException;
import com.scriptbasic.interfaces.Command;
import com.scriptbasic.interfaces.CommandAnalyzer;
import com.scriptbasic.interfaces.CommandFactory;
import com.scriptbasic.interfaces.Factory;
import com.scriptbasic.interfaces.LexicalAnalyzer;
import com.scriptbasic.interfaces.LineOrientedLexicalAnalyzer;
import com.scriptbasic.log.Logger;
import com.scriptbasic.log.LoggerFactory;
import com.scriptbasic.utility.FactoryUtility;

public final class BasicCommandFactory implements CommandFactory {
    private static final Logger LOG = LoggerFactory
            .getLogger();
    private Factory factory;

    public Factory getFactory() {
        return factory;
    }

    @Override
    public void setFactory(Factory factory) {
        this.factory = factory;
        registerCommandAnalyzer("while", new CommandAnalyzerWhile());
        registerCommandAnalyzer("wend", new CommandAnalyzerWend());
        registerCommandAnalyzer("if", new CommandAnalyzerIf());
        registerCommandAnalyzer("else", new CommandAnalyzerElse());
        registerCommandAnalyzer("elseif", new CommandAnalyzerElseIf());
        registerCommandAnalyzer("endif", new CommandAnalyzerEndIf());
        registerCommandAnalyzer("use", new CommandAnalyzerUse());
        registerCommandAnalyzer("method", new CommandAnalyzerMethod());
        registerCommandAnalyzer("sub", new CommandAnalyzerSub());
        registerCommandAnalyzer("endsub", new CommandAnalyzerEndSub());
        registerCommandAnalyzer("return", new CommandAnalyzerReturn());
        registerCommandAnalyzer("print", new CommandAnalyzerPrint());
        registerCommandAnalyzer("local", new CommandAnalyzerLocal());
        registerCommandAnalyzer("global", new CommandAnalyzerGlobal());
        registerCommandAnalyzer("call", new CommandAnalyzerCall());
        registerCommandAnalyzer("let", new CommandAnalyzerLet());
        registerCommandAnalyzer("for", new CommandAnalyzerFor());
        registerCommandAnalyzer("next", new CommandAnalyzerNext());
        //
        registerCommandAnalyzer(new CommandAnalyzerLet());
        registerCommandAnalyzer(new CommandAnalyzerCall());
    }

    private Map<String, CommandAnalyzer> classMap = new HashMap<String, CommandAnalyzer>();
    private List<CommandAnalyzer> classList = new LinkedList<CommandAnalyzer>();

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

    @Override
    public Command create(final String commandKeyword) throws AnalysisException {
        if (commandKeyword == null) {
            return create();
        } else {
            return createFromStartingSymbol(commandKeyword);
        }
    }

    private Command create() throws AnalysisException {
        LexicalAnalyzer lexicalAnalyzer = FactoryUtility
                .getLexicalAnalyzer(getFactory());
        if (lexicalAnalyzer instanceof LineOrientedLexicalAnalyzer) {
            LineOrientedLexicalAnalyzer loLexicalAnalyzer = (LineOrientedLexicalAnalyzer) lexicalAnalyzer;
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
                loLexicalAnalyzer.resetLine();
            }
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

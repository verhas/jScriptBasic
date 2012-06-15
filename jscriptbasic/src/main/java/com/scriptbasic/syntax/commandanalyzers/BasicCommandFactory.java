package com.scriptbasic.syntax.commandanalyzers;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.scriptbasic.exceptions.AnalysisException;
import com.scriptbasic.exceptions.CommandFactoryException;
import com.scriptbasic.exceptions.KeywordNotImplementedException;
import com.scriptbasic.interfaces.Command;
import com.scriptbasic.interfaces.CommandAnalyzer;
import com.scriptbasic.interfaces.CommandFactory;
import com.scriptbasic.interfaces.Factory;
import com.scriptbasic.syntax.commandanalyzers.commands.CommandAnalyzerCall;
import com.scriptbasic.syntax.commandanalyzers.commands.CommandAnalyzerLet;
import com.scriptbasic.syntax.commandanalyzers.commands.CommandAnalyzerWend;
import com.scriptbasic.syntax.commandanalyzers.commands.CommandAnalyzerWhile;

//TODO extend the code to handle the case when there are more than one commands that start with a specific 
// keyword, for example END FUNCTION, END IF
public final class BasicCommandFactory implements CommandFactory {
    private static Logger log = LoggerFactory
            .getLogger(BasicCommandFactory.class);
    Factory factory;

    public Factory getFactory() {
        return factory;
    }

    @Override
    public void setFactory(Factory factory) {
        this.factory = factory;
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
        if (keyword == null) {
            classList.add(analyzer);
        } else {
            classMap.put(keyword, analyzer);
        }
    }

    private void registerCommandAnalyzer(CommandAnalyzer analyzer) {
        registerCommandAnalyzer(null, analyzer);
    }

    private BasicCommandFactory() {
        registerCommandAnalyzer("while", new CommandAnalyzerWhile());
        registerCommandAnalyzer("wend", new CommandAnalyzerWend());

        registerCommandAnalyzer(new CommandAnalyzerLet());
        registerCommandAnalyzer(new CommandAnalyzerCall());
    }

    @Override
    public Command create(final String commandKeyword)
            throws AnalysisException, CommandFactoryException {
        if (commandKeyword == null) {
            return create();
        } else {
            return createFromStartingSymbol(commandKeyword);
        }
    }

    private Command create() throws AnalysisException, CommandFactoryException {
        for (final CommandAnalyzer commandAnalyzer : classList) {
            try {
                log.info("trying to analyze the line using "
                        + commandAnalyzer.getClass());
                final Command command = commandAnalyzer.analyze();
                if (command != null) {
                    return command;
                }
            } catch (AnalysisException e) {
                log.info("Tried but not analyze the line using "
                        + commandAnalyzer.getClass(), e);
            }
        }
        log.info("None of the analyzers could analyze the line");
        throw new CommandFactoryException("The line could not be analyzed");
    }

    private Command createFromStartingSymbol(final String commandKeyword)
            throws AnalysisException {
        if (!classMap.containsKey(commandKeyword)) {
            throw new KeywordNotImplementedException(commandKeyword);
        }

        final CommandAnalyzer commandAnalyzer = classMap.get(commandKeyword);
        return commandAnalyzer.analyze();
    }

}

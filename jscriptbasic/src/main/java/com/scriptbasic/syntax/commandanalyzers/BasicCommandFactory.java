package com.scriptbasic.syntax.commandanalyzers;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.scriptbasic.exceptions.AnalysisException;
import com.scriptbasic.exceptions.CommandFactoryException;
import com.scriptbasic.exceptions.KeywordNotImplementedException;
import com.scriptbasic.interfaces.AnalysisResult;
import com.scriptbasic.interfaces.CommandAnalyzer;
import com.scriptbasic.interfaces.CommandFactory;
import com.scriptbasic.interfaces.Factory;
import com.scriptbasic.syntax.commandanalyzers.commands.CommandAnalyzerCall;
import com.scriptbasic.syntax.commandanalyzers.commands.CommandAnalyzerLet;
import com.scriptbasic.syntax.commandanalyzers.commands.CommandAnalyzerWend;
import com.scriptbasic.syntax.commandanalyzers.commands.CommandAnalyzerWhile;

public class BasicCommandFactory implements CommandFactory {
    Factory factory;

    public Factory getFactory() {
        return factory;
    }

    @Override
	public void setFactory(Factory factory) {
        this.factory = factory;
    }

    private Map<String, CommandAnalyzer<?>> classMap = new HashMap<String, CommandAnalyzer<?>>();
    private List<CommandAnalyzer<?>> classList = new LinkedList<CommandAnalyzer<?>>();

    private BasicCommandFactory() {
        classMap.put("while", new CommandAnalyzerWhile());
        classMap.put("wend", new CommandAnalyzerWend());

        classList.add(new CommandAnalyzerLet());
        classList.add(new CommandAnalyzerCall());
    }

    @Override
    public AnalysisResult create(final String commandKeyword)
            throws AnalysisException, CommandFactoryException {
        if (commandKeyword == null) {
            return create();
        } else {
            return createFromStartingSymbol(commandKeyword);
        }
    }

    private AnalysisResult create() throws AnalysisException,
            CommandFactoryException {
        for (final CommandAnalyzer<?> commandAnalyzer : classList) {

            final AnalysisResult analysisResult = commandAnalyzer.analyze();
            if (analysisResult != null) {
                return analysisResult;
            }
        }
        throw new CommandFactoryException("The line could not be analyzed");
    }

    private AnalysisResult createFromStartingSymbol(final String commandKeyword)
            throws AnalysisException {
        if (!classMap.containsKey(commandKeyword)) {
            throw new KeywordNotImplementedException(commandKeyword);
        }

        final CommandAnalyzer<?> commandAnalyzer = classMap.get(commandKeyword);
        return commandAnalyzer.analyze();
    }
}

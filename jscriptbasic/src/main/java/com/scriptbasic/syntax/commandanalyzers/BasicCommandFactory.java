package com.scriptbasic.syntax.commandanalyzers;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.scriptbasic.interfaces.CommandAnalyzer;
import com.scriptbasic.interfaces.CommandFactory;
import com.scriptbasic.interfaces.LexicalException;
import com.scriptbasic.interfaces.AnalysisResult;
import com.scriptbasic.interfaces.SyntaxException;
import com.scriptbasic.syntax.BasicSyntaxAnalyzer;
import com.scriptbasic.syntax.GenericSyntaxException;
import com.scriptbasic.syntax.KeywordNotImplemented;

public class BasicCommandFactory implements CommandFactory {

    BasicSyntaxAnalyzer syntaxAnalyzer;

    public void setSyntaxAnalyzer(final BasicSyntaxAnalyzer basicSyntaxAnalyzer) {
        this.syntaxAnalyzer = basicSyntaxAnalyzer;

    }

    private Map<String, CommandAnalyzer> classMap = new HashMap<String, CommandAnalyzer>();
    private List<CommandAnalyzer> classList = new LinkedList<CommandAnalyzer>();

    public BasicCommandFactory() {
        classMap.put("while", new CommandAnalyzerWhile());

        classList.add(new CommandAnalyzerLet());
        classList.add(new CommandAnalyzerCall());
    }

    @Override
    public AnalysisResult create(final String commandKeyword)
            throws SyntaxException, LexicalException {
        if (commandKeyword == null) {
            return create();
        } else {
            return createFromStartingSymbol(commandKeyword);
        }
    }

    private AnalysisResult create() throws SyntaxException,LexicalException {
        for (final CommandAnalyzer commandAnalyzer : classList) {

            final AnalysisResult analysisResult = commandAnalyzer.analyze();
            if (analysisResult != null) {
                return analysisResult;
            }
        }
        final SyntaxException se = new GenericSyntaxException(
                "Generic syntax exception");
        se.setLocation(this.syntaxAnalyzer.getLexicalElement());
        throw se;
    }

    private AnalysisResult createFromStartingSymbol(final String commandKeyword)
            throws SyntaxException, LexicalException {
        if (!classMap.containsKey(commandKeyword)) {
            final SyntaxException se = new KeywordNotImplemented(commandKeyword);
            se.setLocation(this.syntaxAnalyzer.getLexicalElement());
            throw se;
        }

        final CommandAnalyzer commandAnalyzer = classMap.get(commandKeyword);
        return commandAnalyzer.analyze();
    }
}

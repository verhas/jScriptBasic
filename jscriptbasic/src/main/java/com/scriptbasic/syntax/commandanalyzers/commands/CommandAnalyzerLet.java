package com.scriptbasic.syntax.commandanalyzers.commands;

import com.scriptbasic.exceptions.GenericSyntaxException;
import com.scriptbasic.executors.commands.CommandLet;
import com.scriptbasic.interfaces.AnalysisException;
import com.scriptbasic.interfaces.Command;
import com.scriptbasic.interfaces.LexicalAnalyzer;
import com.scriptbasic.interfaces.LexicalElement;
import com.scriptbasic.syntax.commandanalyzers.AbstractCommandAnalyzer;
import com.scriptbasic.utility.FactoryUtilities;

public class CommandAnalyzerLet extends AbstractCommandAnalyzer {

    @Override
    public Command analyze() throws AnalysisException {
        CommandLet commandLet = new CommandLet();
        LexicalAnalyzer lexicalAnalyzer = FactoryUtilities
                .getLexicalAnalyzer(getFactory());
        LexicalElement lexicalElement = lexicalAnalyzer.peek();
        if (lexicalElement != null && lexicalElement.isSymbol(getName())) {
            lexicalAnalyzer.get();
        }
        commandLet.setLeftValue(FactoryUtilities.getLeftValueAnalyzer(
                getFactory()).analyze());
        lexicalElement = lexicalAnalyzer.get();
        if (lexicalElement == null || !lexicalElement.isSymbol("=")) {
            throw new GenericSyntaxException("Assignment does not contain '='",
                    lexicalElement, null);
        }
        commandLet.setExpression(FactoryUtilities.getExpressionAnalyzer(
                getFactory()).analyze());
        consumeEndOfLine();
        return commandLet;
    }

    @Override
    protected String getName() {
        return "LET";
    }

}

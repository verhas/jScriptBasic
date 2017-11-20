package com.scriptbasic.syntax.commands;

import com.scriptbasic.exceptions.GenericSyntaxException;
import com.scriptbasic.executors.commands.CommandLet;
import com.scriptbasic.interfaces.AnalysisException;
import com.scriptbasic.interfaces.Command;
import com.scriptbasic.interfaces.LexicalAnalyzer;
import com.scriptbasic.interfaces.LexicalElement;
import com.scriptbasic.utility.FactoryUtility;

public class CommandAnalyzerLet extends AbstractCommandAnalyzer {

    @Override
    public Command analyze() throws AnalysisException {
        CommandLet commandLet = new CommandLet();
        LexicalAnalyzer lexicalAnalyzer = FactoryUtility
                .getLexicalAnalyzer(getFactory());
        LexicalElement lexicalElement = lexicalAnalyzer.peek();
        if (lexicalElement != null && lexicalElement.isSymbol(getName())) {
            lexicalAnalyzer.get();
        }
        commandLet.setLeftValue(FactoryUtility.getLeftValueAnalyzer(
                getFactory()).analyze());
        lexicalElement = lexicalAnalyzer.get();
        if (lexicalElement == null || !lexicalElement.isSymbol("=")) {
            throw new GenericSyntaxException("Assignment does not contain '='",
                    lexicalElement, null);
        }
        commandLet.setExpression(FactoryUtility.getExpressionAnalyzer(
                getFactory()).analyze());
        consumeEndOfLine();
        return commandLet;
    }

}

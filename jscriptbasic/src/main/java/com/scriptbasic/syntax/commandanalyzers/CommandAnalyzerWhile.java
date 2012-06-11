package com.scriptbasic.syntax.commandanalyzers;

import com.scriptbasic.executors.commands.CommandWhile;
import com.scriptbasic.interfaces.Command;
import com.scriptbasic.interfaces.Expression;
import com.scriptbasic.interfaces.LexicalElement;
import com.scriptbasic.interfaces.LexicalException;
import com.scriptbasic.interfaces.SyntaxException;
import com.scriptbasic.utility.FactoryUtilities;
import com.scriptbasic.utility.SyntaxExceptionUtility;

public class CommandAnalyzerWhile extends AbstractCommandAnalyzer {

    @Override
    public Command analyze() throws SyntaxException, LexicalException {
        CommandWhile node = new CommandWhile();
        Expression condition = FactoryUtilities.getExpressionAnalyzer(factory)
                .analyze();
        LexicalElement lexicalElement = FactoryUtilities.getLexicalAnalyzer(factory)
                .get();
        if (!lexicalElement.isLineTerminator()) {
            SyntaxExceptionUtility
                    .throwSyntaxException(
                            "There are extra characters following the expression after the 'WHILE' keyword",
                            lexicalElement);
            return null;
        }
        node.setCondition(condition);
        return node;
    }

}

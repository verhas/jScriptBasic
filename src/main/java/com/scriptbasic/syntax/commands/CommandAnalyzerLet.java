package com.scriptbasic.syntax.commands;

import com.scriptbasic.context.Context;
import com.scriptbasic.executors.commands.CommandLet;
import com.scriptbasic.interfaces.AnalysisException;
import com.scriptbasic.interfaces.BasicSyntaxException;
import com.scriptbasic.spi.Command;
import com.scriptbasic.interfaces.LexicalElement;

public class CommandAnalyzerLet extends AbstractCommandAnalyzer {

    public CommandAnalyzerLet(final Context ctx) {
        super(ctx);
    }

    @Override
    public Command analyze() throws AnalysisException {
        final CommandLet commandLet = new CommandLet();
        LexicalElement lexicalElement = ctx.lexicalAnalyzer.peek();
        if (lexicalElement != null && lexicalElement.isSymbol(getName())) {
            ctx.lexicalAnalyzer.get();
        }
        commandLet.setLeftValue(ctx.leftValueAnalyzer.analyze());
        lexicalElement = ctx.lexicalAnalyzer.get();
        if (lexicalElement == null || !lexicalElement.isSymbol("=")) {
            throw new BasicSyntaxException("Assignment does not contain '='",
                    lexicalElement, null);
        }
        commandLet.setExpression(ctx.expressionAnalyzer.analyze());
        consumeEndOfLine();
        return commandLet;
    }

}

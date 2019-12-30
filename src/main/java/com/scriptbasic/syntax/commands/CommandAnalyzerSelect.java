package com.scriptbasic.syntax.commands;

import com.scriptbasic.context.Context;
import com.scriptbasic.executors.commands.CommandSelect;
import com.scriptbasic.interfaces.AnalysisException;
import com.scriptbasic.interfaces.BasicSyntaxException;
import com.scriptbasic.interfaces.ScriptBasicKeyWords;
import com.scriptbasic.spi.Command;
import com.scriptbasic.syntax.BasicSyntaxAnalyzer;

public class CommandAnalyzerSelect
        extends AbstractCommandAnalyzer {

    public CommandAnalyzerSelect(Context ctx) {
        super(ctx);
    }

    @Override
    public Command analyze() throws AnalysisException {
        var lexicalElement = ctx.lexicalAnalyzer.peek();
        // consume optional case statement
        if (lexicalElement.isSymbol(ScriptBasicKeyWords.KEYWORD_CASE))
            ctx.lexicalAnalyzer.get();
        // read expression till end of line
        final var expression = analyzeExpression();
        consumeEndOfLine();

        final var node = new CommandSelect();
        node.setExpression(expression);
        pushNode(node);

        // next command has to be 'case'
        // first skip any comments
        lexicalElement = ctx.lexicalAnalyzer.peek();
        while (lexicalElement != null && BasicSyntaxAnalyzer.lineToIgnore(lexicalElement.getLexeme())) {
            ctx.lexicalAnalyzer.get();
            BasicSyntaxAnalyzer.consumeIgnoredLine(ctx.lexicalAnalyzer, lexicalElement.getLexeme());
            lexicalElement = ctx.lexicalAnalyzer.peek();
        }
        if (lexicalElement == null) {
            throw new BasicSyntaxException("Preliminary end of file");
        }
        if (!lexicalElement.isSymbol(ScriptBasicKeyWords.KEYWORD_CASE)) {
            throw new BasicSyntaxException("Expected case statement, but found: " + lexicalElement.getLexeme());
        }

        return node;
    }

}

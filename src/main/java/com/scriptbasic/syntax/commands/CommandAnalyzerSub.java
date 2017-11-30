package com.scriptbasic.syntax.commands;

import com.scriptbasic.exceptions.GenericSyntaxException;
import com.scriptbasic.executors.commands.CommandSub;
import com.scriptbasic.factories.Context;
import com.scriptbasic.interfaces.AnalysisException;
import com.scriptbasic.interfaces.Command;
import com.scriptbasic.interfaces.LeftValueList;
import com.scriptbasic.interfaces.LexicalElement;

public class CommandAnalyzerSub extends AbstractCommandAnalyzer {
    public CommandAnalyzerSub(Context ctx) {
        super(ctx);
    }

    @Override
    public Command analyze() throws AnalysisException {
        CommandSub node = new CommandSub();

        LexicalElement lexicalElement = ctx.lexicalAnalyzer.get();
        if (lexicalElement.isIdentifier()) {
            String subName = lexicalElement.getLexeme();
            node.setSubName(subName);
        } else {
            throw new GenericSyntaxException(
                    "subroutine name has to follow the keyword " + getName());
        }
        if (isKeyWord("(")) {
            ctx.lexicalAnalyzer.get();
            if (isKeyWord(")")) {
                node.setArguments(null);
                ctx.lexicalAnalyzer.get();
            } else {
                LeftValueList arguments = analyzeSimpleLeftValueList();
                node.setArguments(arguments);
                assertKeyWord(")");
            }
        } else {
            node.setArguments(null);
        }
        pushNode(node);
        consumeEndOfLine();
        return node;
    }
}

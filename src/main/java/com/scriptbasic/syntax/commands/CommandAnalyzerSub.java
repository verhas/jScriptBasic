package com.scriptbasic.syntax.commands;

import com.scriptbasic.api.Command;
import com.scriptbasic.api.LeftValueList;
import com.scriptbasic.context.Context;
import com.scriptbasic.executors.commands.CommandSub;
import com.scriptbasic.interfaces.*;

public class CommandAnalyzerSub extends AbstractCommandAnalyzer {
    public CommandAnalyzerSub(final Context ctx) {
        super(ctx);
    }

    @Override
    public Command analyze() throws AnalysisException {
        final CommandSub node = new CommandSub();

        final LexicalElement lexicalElement = ctx.lexicalAnalyzer.get();
        if (lexicalElement.isIdentifier()) {
            final String subName = lexicalElement.getLexeme();
            node.setSubName(subName);
        } else {
            throw new BasicSyntaxException(
                    "subroutine name has to follow the keyword " + getName());
        }
        if (isKeyWord("(")) {
            ctx.lexicalAnalyzer.get();
            if (isKeyWord(")")) {
                node.setArguments(null);
                ctx.lexicalAnalyzer.get();
            } else {
                final LeftValueList arguments = analyzeSimpleLeftValueList();
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

package com.scriptbasic.syntax.commands;

import com.scriptbasic.spi.Command;
import com.scriptbasic.context.Context;
import com.scriptbasic.executors.commands.CommandSub;
import com.scriptbasic.interfaces.*;

public class CommandAnalyzerSub extends AbstractCommandAnalyzer {
    public CommandAnalyzerSub(final Context ctx) {
        super(ctx);
    }

    @Override
    public Command analyze() throws AnalysisException {
        final var node = new CommandSub();

        final var lexicalElement = ctx.lexicalAnalyzer.get();
        if (lexicalElement.isIdentifier()) {
            final var subName = lexicalElement.getLexeme();
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
                final var arguments = analyzeSimpleLeftValueList();
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

package com.scriptbasic.syntax.commands;

import com.scriptbasic.context.Context;
import com.scriptbasic.executors.commands.AbstractCommandIfElseKind;
import com.scriptbasic.executors.commands.CommandElse;
import com.scriptbasic.interfaces.AnalysisException;
import com.scriptbasic.interfaces.BasicSyntaxException;
import com.scriptbasic.spi.Command;

public class CommandAnalyzerElse extends AbstractCommandAnalyzerIfElseKind {

    public CommandAnalyzerElse(final Context ctx) {
        super(ctx);
    }

    @Override
    public Command analyze() throws AnalysisException {
        final var node = new CommandElse();
        AbstractCommandIfElseKind prevNode = registerAndPopNode(node);
        if(prevNode instanceof CommandElse) {
            throw new BasicSyntaxException("Multiple 'else' statements", ctx.lexicalAnalyzer.peek());
        }
        pushNode(node);
        consumeEndOfStatement();
        return node;
    }

}

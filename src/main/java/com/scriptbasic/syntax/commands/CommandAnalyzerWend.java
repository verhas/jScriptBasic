package com.scriptbasic.syntax.commands;

import com.scriptbasic.context.Context;
import com.scriptbasic.executors.commands.CommandWend;
import com.scriptbasic.executors.commands.CommandWhile;
import com.scriptbasic.interfaces.AnalysisException;
import com.scriptbasic.interfaces.Command;

public class CommandAnalyzerWend extends AbstractCommandAnalyzer {

    public CommandAnalyzerWend(final Context ctx) {
        super(ctx);
    }

    @Override
    public Command analyze() throws AnalysisException {
        final CommandWend node = new CommandWend();
        consumeEndOfLine();
        final CommandWhile commandWhile = ctx.nestedStructureHouseKeeper.pop(CommandWhile.class);
        node.setCommandWhile(commandWhile);
        commandWhile.setWendNode(node);
        return node;
    }
}

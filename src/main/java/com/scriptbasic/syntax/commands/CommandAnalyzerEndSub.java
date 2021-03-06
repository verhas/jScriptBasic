package com.scriptbasic.syntax.commands;

import com.scriptbasic.context.Context;
import com.scriptbasic.executors.commands.CommandEndSub;
import com.scriptbasic.executors.commands.CommandSub;
import com.scriptbasic.interfaces.AnalysisException;
import com.scriptbasic.spi.Command;

public class CommandAnalyzerEndSub extends AbstractCommandAnalyzer {

    public CommandAnalyzerEndSub(final Context ctx) {
        super(ctx);
    }

    @Override
    public Command analyze() throws AnalysisException {
        final var node = new CommandEndSub();
        consumeEndOfStatement();
        final var commandSub = ctx.nestedStructureHouseKeeper.pop(CommandSub.class);
        commandSub.setCommandEndSub(node);
        return node;
    }
}

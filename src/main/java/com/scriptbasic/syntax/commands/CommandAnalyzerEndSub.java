package com.scriptbasic.syntax.commands;

import com.scriptbasic.executors.commands.CommandEndSub;
import com.scriptbasic.executors.commands.CommandSub;
import com.scriptbasic.factories.Context;
import com.scriptbasic.interfaces.AnalysisException;
import com.scriptbasic.interfaces.Command;

public class CommandAnalyzerEndSub extends AbstractCommandAnalyzer {

    public CommandAnalyzerEndSub(Context ctx) {
        super(ctx);
    }

    @Override
    public Command analyze() throws AnalysisException {
        CommandEndSub node = new CommandEndSub();
        consumeEndOfLine();
        CommandSub commandSub = ctx.nestedStructureHouseKeeper.pop(CommandSub.class);
        commandSub.setCommandEndSub(node);
        return node;
    }
}

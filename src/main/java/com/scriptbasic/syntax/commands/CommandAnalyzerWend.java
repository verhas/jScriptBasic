package com.scriptbasic.syntax.commands;

import com.scriptbasic.executors.commands.CommandWend;
import com.scriptbasic.executors.commands.CommandWhile;
import com.scriptbasic.factories.Context;
import com.scriptbasic.interfaces.AnalysisException;
import com.scriptbasic.interfaces.Command;

public class CommandAnalyzerWend extends AbstractCommandAnalyzer {

    public CommandAnalyzerWend(Context ctx) {
        super(ctx);
    }

    @Override
    public Command analyze() throws AnalysisException {
        CommandWend node = new CommandWend();
        consumeEndOfLine();
        CommandWhile commandWhile = ctx.nestedStructureHouseKeeper.pop(CommandWhile.class);
        node.setCommandWhile(commandWhile);
        commandWhile.setWendNode(node);
        return node;
    }
}

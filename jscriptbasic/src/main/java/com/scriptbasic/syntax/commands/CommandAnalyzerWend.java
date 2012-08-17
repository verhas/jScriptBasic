package com.scriptbasic.syntax.commands;

import com.scriptbasic.executors.commands.CommandWend;
import com.scriptbasic.executors.commands.CommandWhile;
import com.scriptbasic.interfaces.AnalysisException;
import com.scriptbasic.interfaces.Command;
import com.scriptbasic.interfaces.NestedStructureHouseKeeper;
import com.scriptbasic.utility.FactoryUtility;

public class CommandAnalyzerWend extends AbstractCommandAnalyzer {

    @Override
    public Command analyze() throws AnalysisException {
        CommandWend node = new CommandWend();
        consumeEndOfLine();
        NestedStructureHouseKeeper nshk = FactoryUtility
                .getNestedStructureHouseKeeper(getFactory());
        CommandWhile commandWhile = nshk.pop(CommandWhile.class);
        node.setCommandWhile(commandWhile);
        commandWhile.setWendNode(node);
        return node;
    }

    @Override
	protected String getName() {
        return "WEND";
    }
}

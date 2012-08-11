package com.scriptbasic.syntax.commands;
import com.scriptbasic.executors.commands.CommandEndSub;
import com.scriptbasic.executors.commands.CommandSub;
import com.scriptbasic.interfaces.AnalysisException;
import com.scriptbasic.interfaces.Command;
import com.scriptbasic.interfaces.NestedStructureHouseKeeper;
import com.scriptbasic.utility.FactoryUtility;
public class CommandAnalyzerEndSub extends AbstractCommandAnalyzer {
    @Override
    public Command analyze() throws AnalysisException {
        CommandEndSub node = new CommandEndSub();
        consumeEndOfLine();
        NestedStructureHouseKeeper nshk = FactoryUtility
                .getNestedStructureHouseKeeper(getFactory());
        CommandSub commandSub = nshk.pop(CommandSub.class);
        commandSub.setCommandEndSub(node);
        return node;
    }
    @Override
    protected String getName() {
        return "ELSE";
    }
}
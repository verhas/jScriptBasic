package com.scriptbasic.syntax.commandanalyzers.commands;

import com.scriptbasic.exceptions.AnalysisException;
import com.scriptbasic.executors.commands.CommandElse;
import com.scriptbasic.executors.commands.CommandIf;
import com.scriptbasic.interfaces.Command;
import com.scriptbasic.interfaces.NestedStructureHouseKeeper;
import com.scriptbasic.syntax.commandanalyzers.AbstractCommandAnalyzer;
import com.scriptbasic.utility.FactoryUtilities;

public class CommandAnalyzerElse extends AbstractCommandAnalyzer {

    @Override
    public Command analyze() throws AnalysisException {
        CommandElse node = new CommandElse();
        assertThereAreNoSuperflouosCharactersOnTheLine();
        NestedStructureHouseKeeper nshk = FactoryUtilities
                .getNestedStructureHouseKeeper(getFactory());
        CommandIf commandIf = nshk.pop(CommandIf.class);
        commandIf.setElseNode(node);
        commandIf.setEndIfNode(null);
        pushNodeOnTheAnalysisStack(node);
        return node;
    }

    @Override
    protected String getName() {
        return "ELSE";
    }
}

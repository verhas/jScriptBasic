package com.scriptbasic.syntax.commandanalyzers.commands;

import com.scriptbasic.executors.commands.CommandEndIf;
import com.scriptbasic.executors.commands.CommandIf;
import com.scriptbasic.executors.commands.IfOrElse;
import com.scriptbasic.interfaces.AnalysisException;
import com.scriptbasic.interfaces.Command;
import com.scriptbasic.interfaces.NestedStructureHouseKeeper;
import com.scriptbasic.syntax.commandanalyzers.AbstractCommandAnalyzer;
import com.scriptbasic.utility.FactoryUtilities;

public class CommandAnalyzerEndIf extends AbstractCommandAnalyzer {

    @Override
    public Command analyze() throws AnalysisException {
        CommandEndIf node = new CommandEndIf();
        consumeEndOfLine();
        NestedStructureHouseKeeper nshk = FactoryUtilities
                .getNestedStructureHouseKeeper(getFactory());
        IfOrElse commandIfOrElse = nshk.pop(IfOrElse.class);
        if (commandIfOrElse instanceof CommandIf) {
            CommandIf commandIf = (CommandIf) commandIfOrElse;
            commandIf.setElseNode(null);
            commandIf.setEndIfNode(node);
        }
        return node;
    }

    @Override
    protected String getName() {
        return "ELSE";
    }
}

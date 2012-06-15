package com.scriptbasic.syntax.commandanalyzers.commands;

import com.scriptbasic.exceptions.AnalysisException;
import com.scriptbasic.executors.commands.CommandWhile;
import com.scriptbasic.interfaces.Command;
import com.scriptbasic.interfaces.Expression;
import com.scriptbasic.interfaces.NestedStructureHouseKeeper;
import com.scriptbasic.syntax.commandanalyzers.AbstractCommandAnalyzer;
import com.scriptbasic.utility.FactoryUtilities;

public class CommandAnalyzerWhile extends AbstractCommandAnalyzer {

    @Override
    public Command analyze() throws AnalysisException {
        CommandWhile node = new CommandWhile();
        Expression condition = FactoryUtilities.getExpressionAnalyzer(getFactory())
                .analyze();
        assertThereAreNoSuperflouosCharactersOnTheLine();
        node.setCondition(condition);
        NestedStructureHouseKeeper nshk = FactoryUtilities.getNestedStructureHouseKeeper(getFactory());
        nshk.push(node);
        return node;
    }

    @Override
	protected String getName() {
        return "WHILE";
    }
}

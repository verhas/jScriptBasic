package com.scriptbasic.syntax.commandanalyzers.commands;

import com.scriptbasic.executors.commands.CommandWhile;
import com.scriptbasic.interfaces.AnalysisException;
import com.scriptbasic.interfaces.Command;
import com.scriptbasic.interfaces.Expression;
import com.scriptbasic.syntax.commandanalyzers.AbstractCommandAnalyzer;

public class CommandAnalyzerWhile extends AbstractCommandAnalyzer {

    @Override
    public Command analyze() throws AnalysisException {
        CommandWhile node = new CommandWhile();
        Expression condition = analyzeExpression();
        consumeEndOfLine();
        node.setCondition(condition);
        pushNodeOnTheAnalysisStack(node);
        return node;
    }

    @Override
    protected String getName() {
        return "WHILE";
    }
}

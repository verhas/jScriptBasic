package com.scriptbasic.syntax.commandanalyzers;

import com.scriptbasic.executors.commands.CommandWhile;
import com.scriptbasic.interfaces.AnalysisException;
import com.scriptbasic.interfaces.Command;
import com.scriptbasic.interfaces.Expression;

public class CommandAnalyzerWhile extends AbstractCommandAnalyzer {

    @Override
    public Command analyze() throws AnalysisException {
        CommandWhile node = new CommandWhile();
        Expression condition = analyzeExpression();
        consumeEndOfLine();
        node.setCondition(condition);
        pushNode(node);
        return node;
    }

    @Override
    protected String getName() {
        return "WHILE";
    }
}

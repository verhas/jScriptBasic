package com.scriptbasic.syntax.commands;

import com.scriptbasic.context.Context;
import com.scriptbasic.executors.commands.CommandEndIf;
import com.scriptbasic.interfaces.AnalysisException;
import com.scriptbasic.spi.Command;

public class CommandAnalyzerEndIf extends AbstractCommandAnalyzerIfElseKind {

    public CommandAnalyzerEndIf(final Context ctx) {
        super(ctx);
    }

    @Override
    public Command analyze() throws AnalysisException {
        final CommandEndIf node = new CommandEndIf();
        consumeEndOfLine();
        registerAndPopNode(node);
        return node;
    }

}

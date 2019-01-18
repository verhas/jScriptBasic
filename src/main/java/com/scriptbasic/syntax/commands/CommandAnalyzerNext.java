package com.scriptbasic.syntax.commands;

import com.scriptbasic.context.Context;
import com.scriptbasic.executors.commands.CommandFor;
import com.scriptbasic.executors.commands.CommandNext;
import com.scriptbasic.interfaces.AnalysisException;
import com.scriptbasic.interfaces.BasicSyntaxException;
import com.scriptbasic.spi.Command;

/**
 * @author Peter Verhas
 * date June 16, 2012
 */
public class CommandAnalyzerNext extends AbstractCommandAnalyzer {

    public CommandAnalyzerNext(final Context ctx) {
        super(ctx);
    }

    @Override
    public Command analyze() throws AnalysisException {
        final var node = new CommandNext();
        final var lexicalElement = ctx.lexicalAnalyzer.peek();
        final var commandFor = ctx.nestedStructureHouseKeeper.pop(CommandFor.class);
        commandFor.setLoopEndNode(node);
        node.setLoopStartNode(commandFor);
        if (lexicalElement != null && !lexicalElement.isLineTerminator()) {
            final var loopVariableB = analyzeSimpleLeftValue();
            final var loopVariableA = commandFor.getLoopVariable();
            if (!equal(loopVariableA, loopVariableB)) {
                throw new BasicSyntaxException(
                        "The variable following the command "
                                + getName()
                                + " does not match the loop variable of the corresponging command FOR",
                        lexicalElement);
            }
        }
        consumeEndOfLine();
        return node;
    }
}

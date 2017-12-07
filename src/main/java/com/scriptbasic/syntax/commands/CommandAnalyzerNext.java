package com.scriptbasic.syntax.commands;

import com.scriptbasic.spi.Command;
import com.scriptbasic.spi.LeftValue;
import com.scriptbasic.context.Context;
import com.scriptbasic.executors.commands.CommandFor;
import com.scriptbasic.executors.commands.CommandNext;
import com.scriptbasic.interfaces.*;

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
        final CommandNext node = new CommandNext();
        final LexicalElement lexicalElement = ctx.lexicalAnalyzer.peek();
        final CommandFor commandFor = ctx.nestedStructureHouseKeeper.pop(CommandFor.class);
        commandFor.setLoopEndNode(node);
        node.setLoopStartNode(commandFor);
        if (lexicalElement != null && !lexicalElement.isLineTerminator()) {
            final LeftValue loopVariableB = analyzeSimpleLeftValue();
            final LeftValue loopVariableA = commandFor.getLoopVariable();
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

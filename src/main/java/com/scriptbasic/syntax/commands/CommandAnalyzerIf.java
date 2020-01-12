package com.scriptbasic.syntax.commands;

import com.scriptbasic.context.Context;
import com.scriptbasic.executors.commands.AbstractCommandIfElseKind;
import com.scriptbasic.executors.commands.CommandEndIf;
import com.scriptbasic.executors.commands.CommandIf;
import com.scriptbasic.interfaces.AnalysisException;
import com.scriptbasic.interfaces.BasicSyntaxException;
import com.scriptbasic.interfaces.ScriptBasicKeyWords;
import com.scriptbasic.interfaces.NestedStructureHouseKeeper.EndOfStatementProcessor;
import com.scriptbasic.interfaces.NestedStructureHouseKeeper.EndOfStatementResult;
import com.scriptbasic.spi.Command;
import com.scriptbasic.syntax.BasicSyntaxAnalyzer;

/**
 * @author Peter Verhas
 * date June 16, 2012
 */
public class CommandAnalyzerIf extends AbstractCommandAnalyzerIfKind {
    
    enum InlineProcessorState {
        EXPECTING_THEN_CLAUSE,
        EXPECTING_ELSE,
        EXPECTING_ELSE_CLAUSE,
        CLAUSE_DEFINED
    }
    
    /**
     * Processor for inline 'if' statement
     * 
     */
    class InlineProcessor implements EndOfStatementProcessor {
                
        InlineProcessorState state = InlineProcessorState.EXPECTING_THEN_CLAUSE;

        @Override
        public EndOfStatementResult consumeEndOfStatement() throws AnalysisException {
            // else statement was processed
            if(state == InlineProcessorState.EXPECTING_ELSE) {
                state = InlineProcessorState.EXPECTING_ELSE_CLAUSE;
                return EndOfStatementResult.CONSUMED;
            }
            
            final var nextElement = ctx.lexicalAnalyzer.peek();
            if(nextElement!=null) {
                // handle multiple statements separated with colon
                if(nextElement.getLexeme().equals(":")) {
                    ctx.lexicalAnalyzer.get();
                    return EndOfStatementResult.CONSUMED;
                }
            
                if (!nextElement.isLineTerminator() && !nextElement.getLexeme().equals("'")
                        && state == InlineProcessorState.EXPECTING_THEN_CLAUSE) {
                    // only else statement is allowed
                    if (nextElement.isSymbol(ScriptBasicKeyWords.KEYWORD_ELSE)) {
                        state = InlineProcessorState.EXPECTING_ELSE;
                        return EndOfStatementResult.CONSUMED;
                    }
                    throw new BasicSyntaxException("Unexpexted element: " + nextElement.getLexeme());
                }
            }
            
            state = InlineProcessorState.CLAUSE_DEFINED;

            // finish this if statement
            final var proccessor = ctx.nestedStructureHouseKeeper.popEndOfStatementProcessor();
            if(proccessor!=this) {
                throw new BasicSyntaxException("Unexpexted state."); 
            }
            final var cmd = ctx.nestedStructureHouseKeeper.pop(AbstractCommandIfElseKind.class);

            final var commandEndIf = new CommandEndIf();
            cmd.setNext(commandEndIf);
            ctx.syntaxAnalyzer.addCommand(commandEndIf);
            return EndOfStatementResult.PROCESSED;
        }
        
    }

    public CommandAnalyzerIf(final Context ctx) {
        super(ctx);
    }

    @Override
    public Command analyze() throws AnalysisException {
        var condition = analyzeCondition();
        
        final var node = new CommandIf();
        node.setCondition(condition);
        pushNode(node);

        final var nextElement = ctx.lexicalAnalyzer.peek();
        // check if inline version of if statement
        if (nextElement != null && !nextElement.isLineTerminator() 
                && !BasicSyntaxAnalyzer.lineToIgnore(nextElement.getLexeme())) {
            ctx.nestedStructureHouseKeeper.pushEndOfStatementProcessor(new InlineProcessor());
        } else {
            consumeEndOfStatement();
        }
        return node;
    }
}

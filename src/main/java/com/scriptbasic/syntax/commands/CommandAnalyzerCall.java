package com.scriptbasic.syntax.commands;

import com.scriptbasic.context.Context;
import com.scriptbasic.executors.commands.CommandCall;
import com.scriptbasic.executors.commands.CommandLet;
import com.scriptbasic.executors.leftvalues.BasicLeftValue;
import com.scriptbasic.executors.rightvalues.FunctionCall;
import com.scriptbasic.interfaces.AnalysisException;
import com.scriptbasic.interfaces.BasicSyntaxException;
import com.scriptbasic.interfaces.LexicalAnalyzer;
import com.scriptbasic.interfaces.LexicalElement;
import com.scriptbasic.spi.Command;

public class CommandAnalyzerCall extends AbstractCommandAnalyzer {

    public CommandAnalyzerCall(final Context ctx) {
        super(ctx);
    }

    @Override
    public Command analyze() throws AnalysisException {
        skipTheOptionalCallKeyword();

        final var lv = (BasicLeftValue) ctx.leftValueAnalyzer.analyze();
        if (lv.hasModifiers()) {
            ctx.lexicalAnalyzer.resetLine();
            final var commandLet = new CommandLet();
            commandLet.setExpression(ctx.expressionAnalyzer.analyze());
            consumeEndOfStatement();
            return commandLet;
        } else {
            final var functionName = lv.getIdentifier();
            final var functionCall = new FunctionCall();
            functionCall.setVariableName(functionName);

            final var needClosingParenthesis = argumentsAreBetweenParentheses(ctx.lexicalAnalyzer);
            if (needClosingParenthesis) {
                ctx.lexicalAnalyzer.get();// jump over '('
            }

            final var lexicalElement = ctx.lexicalAnalyzer.peek();
            if (thereAreArguments(needClosingParenthesis, lexicalElement)) {
                functionCall.setExpressionList(ctx.expressionListAnalyzer.analyze());
            } else {
                functionCall.setExpressionList(null);
            }
            if (needClosingParenthesis) {
                consumeClosingParenthesis(ctx.lexicalAnalyzer);
            }
            consumeEndOfStatement();
            return new CommandCall(functionCall);
        }
    }

    private boolean thereAreArguments(final boolean needClosingParenthesis, final LexicalElement lexicalElement) {
        return lexicalElement != null && !lexicalElement.isSymbol(needClosingParenthesis ? ")" : "\n");
    }

    private void consumeClosingParenthesis(final LexicalAnalyzer lexicalAnalyzer) throws AnalysisException {
        final var closingParenthesis = lexicalAnalyzer.peek();
        if (closingParenthesis != null && closingParenthesis.isSymbol(")")) {
            lexicalAnalyzer.get();
        } else {
            throw new BasicSyntaxException("The closing ) is missing after the CALL statement");
        }
    }

    private boolean argumentsAreBetweenParentheses(final LexicalAnalyzer lexicalAnalyzer) throws AnalysisException {
        final var openingParenthesis = lexicalAnalyzer.peek();
        return openingParenthesis != null && openingParenthesis.isSymbol("(");
    }

    /**
     * Skip over the keyword CALL.
     * <p>
     * Note that there is no need to check that the keyword is really 'CALL' because if it is not then the
     * execution does not get here. The syntax analyzer invokes CommandAnalyzerCall only when the line starts
     * as a function call.
     *
     * @throws AnalysisException in case of exception
     */
    private void skipTheOptionalCallKeyword() throws AnalysisException {
        final var lexicalElement = ctx.lexicalAnalyzer.peek();
        if (lexicalElement != null && lexicalElement.isSymbol(getName())) {
            ctx.lexicalAnalyzer.get();
        }
    }

}

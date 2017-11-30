package com.scriptbasic.syntax.commands;

import com.scriptbasic.exceptions.GenericSyntaxException;
import com.scriptbasic.executors.commands.CommandCall;
import com.scriptbasic.executors.commands.CommandLet;
import com.scriptbasic.executors.leftvalues.BasicLeftValue;
import com.scriptbasic.executors.rightvalues.FunctionCall;
import com.scriptbasic.factories.Context;
import com.scriptbasic.interfaces.AnalysisException;
import com.scriptbasic.interfaces.Command;
import com.scriptbasic.interfaces.LexicalAnalyzer;
import com.scriptbasic.interfaces.LexicalElement;

public class CommandAnalyzerCall extends AbstractCommandAnalyzer {

    public CommandAnalyzerCall(Context ctx) {
        super(ctx);
    }

    @Override
    public Command analyze() throws AnalysisException {
        skipTheOptionalCallKeyword();

        BasicLeftValue lv = (BasicLeftValue) ctx.leftValueAnalyzer.analyze();
        if (lv.hasModifiers()) {
            ctx.lexicalAnalyzer.resetLine();
            CommandLet commandLet = new CommandLet();
            commandLet.setExpression(ctx.expressionAnalyzer.analyze());
            consumeEndOfLine();
            return commandLet;
        } else {
            final String functionName = lv.getIdentifier();
            FunctionCall functionCall = new FunctionCall();
            functionCall.setVariableName(functionName);

            final boolean needClosingParenthesis = argumentsAreBetweenParentheses(ctx.lexicalAnalyzer);
            if (needClosingParenthesis) {
                ctx.lexicalAnalyzer.get();// jump over '('
            }

            LexicalElement lexicalElement = ctx.lexicalAnalyzer.peek();
            if (thereAreArguments(needClosingParenthesis, lexicalElement)) {
                functionCall.setExpressionList(ctx.expressionListAnalyzer.analyze());
            } else {
                functionCall.setExpressionList(null);
            }
            if (needClosingParenthesis) {
                consumeClosingParenthesis(ctx.lexicalAnalyzer);
            }
            consumeEndOfLine();
            CommandCall node = new CommandCall(functionCall);
            return node;
        }
    }

    private boolean thereAreArguments(boolean needClosingParenthesis, LexicalElement lexicalElement) {
        return lexicalElement != null && !lexicalElement.isSymbol(needClosingParenthesis ? ")" : "\n");
    }

    private void consumeClosingParenthesis(LexicalAnalyzer lexicalAnalyzer) throws AnalysisException {
        final LexicalElement closingParenthesis = lexicalAnalyzer.peek();
        if (closingParenthesis != null && closingParenthesis.isSymbol(")")) {
            lexicalAnalyzer.get();
        } else {
            throw new GenericSyntaxException("The closing ) is missing after the CALL statement");
        }
    }

    private boolean argumentsAreBetweenParentheses(LexicalAnalyzer lexicalAnalyzer) throws AnalysisException {
        final LexicalElement openingParenthesis = lexicalAnalyzer.peek();
        return openingParenthesis != null && openingParenthesis.isSymbol("(");
    }

    /**
     * Skip over the keyword CALL.
     * <p>
     * Note that there is no need to check that the keyword is really 'CALL' because if it is not then the
     * execution does not get here. The syntax analyzer invokes CommandAnalyzerCall only when the line starts
     * as a function call.
     *
     * @throws AnalysisException
     */
    private void skipTheOptionalCallKeyword() throws AnalysisException {
        LexicalElement lexicalElement = ctx.lexicalAnalyzer.peek();
        if (lexicalElement != null && lexicalElement.isSymbol(getName())) {
            ctx.lexicalAnalyzer.get();
        }
    }

}

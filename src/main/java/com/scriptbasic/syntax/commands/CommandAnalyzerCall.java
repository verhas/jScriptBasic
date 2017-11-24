package com.scriptbasic.syntax.commands;

import com.scriptbasic.exceptions.GenericSyntaxException;
import com.scriptbasic.executors.commands.CommandCall;
import com.scriptbasic.executors.commands.CommandLet;
import com.scriptbasic.executors.leftvalues.BasicLeftValue;
import com.scriptbasic.executors.rightvalues.FunctionCall;
import com.scriptbasic.interfaces.*;
import com.scriptbasic.utility.FactoryUtility;

public class CommandAnalyzerCall extends AbstractCommandAnalyzer {

    @Override
    public Command analyze() throws AnalysisException {
        LineOrientedLexicalAnalyzer lexicalAnalyzer =
                (LineOrientedLexicalAnalyzer) FactoryUtility.getLexicalAnalyzer(getFactory());
        skipTheOptionalCallKeyword(lexicalAnalyzer);

        BasicLeftValue lv = (BasicLeftValue) FactoryUtility.getLeftValueAnalyzer(getFactory()).analyze();
        if (lv.hasModifiers()) {
            lexicalAnalyzer.resetLine();
            CommandLet commandLet = new CommandLet();
            commandLet.setExpression(FactoryUtility.getExpressionAnalyzer(getFactory()).analyze());
            consumeEndOfLine();
            return commandLet;
        } else {
            final String functionName = lv.getIdentifier();
            FunctionCall functionCall = new FunctionCall();
            functionCall.setVariableName(functionName);

            final boolean needClosingParenthesis = argumentsAreBetweenParentheses(lexicalAnalyzer);
            if (needClosingParenthesis) {
                lexicalAnalyzer.get();// jump over '('
            }

            LexicalElement lexicalElement = lexicalAnalyzer.peek();
            if (thereAreArguments(needClosingParenthesis, lexicalElement)) {
                functionCall.setExpressionList(FactoryUtility
                        .getExpressionListAnalyzer(getFactory()).analyze());
            } else {
                functionCall.setExpressionList(null);
            }
            if (needClosingParenthesis) {
                consumeClosingParenthesis(lexicalAnalyzer);
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
     * @param lexicalAnalyzer
     * @throws AnalysisException
     */
    private void skipTheOptionalCallKeyword(LexicalAnalyzer lexicalAnalyzer) throws AnalysisException {
        LexicalElement lexicalElement = lexicalAnalyzer.peek();
        if (lexicalElement != null && lexicalElement.isSymbol(getName())) {
            lexicalAnalyzer.get();
        }
    }

}

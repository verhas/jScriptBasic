package com.scriptbasic.syntax.commands;

import com.scriptbasic.exceptions.GenericSyntaxException;
import com.scriptbasic.executors.commands.CommandCall;
import com.scriptbasic.executors.leftvalues.BasicLeftValue;
import com.scriptbasic.executors.rightvalues.FunctionCall;
import com.scriptbasic.interfaces.AnalysisException;
import com.scriptbasic.interfaces.Command;
import com.scriptbasic.interfaces.LexicalAnalyzer;
import com.scriptbasic.interfaces.LexicalElement;
import com.scriptbasic.utility.FactoryUtility;

public class CommandAnalyzerCall extends AbstractCommandAnalyzer {

    @Override
    public Command analyze() throws AnalysisException {
        LexicalAnalyzer lexicalAnalyzer = FactoryUtility.getLexicalAnalyzer(getFactory());
        skipTheOptionalCallKeyword(lexicalAnalyzer);

        FunctionCall functionCall = new FunctionCall();
        functionCall.setVariableName(((BasicLeftValue) FactoryUtility
                .getSimpleLeftValueAnalyzer(getFactory()).analyze())
                .getIdentifier());
        final boolean needClosingParenthesis = argumentsAreBetweenParentheses(lexicalAnalyzer);

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
        if (openingParenthesis != null && openingParenthesis.isSymbol("(")) {
            lexicalAnalyzer.get();
            return true;
        } else {
            return false;
        }
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

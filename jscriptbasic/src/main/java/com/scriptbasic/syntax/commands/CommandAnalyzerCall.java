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
        CommandCall node = new CommandCall();
        LexicalAnalyzer lexicalAnalyzer = FactoryUtility
                .getLexicalAnalyzer(getFactory());
        LexicalElement lexicalElement = lexicalAnalyzer.peek();
        if (lexicalElement != null && lexicalElement.isSymbol(getName())) {
            lexicalAnalyzer.get();
        }
        FunctionCall functionCall = new FunctionCall();
        functionCall.setVariableName(((BasicLeftValue) FactoryUtility
                .getSimpleLeftValueAnalyzer(getFactory()).analyze())
                .getIdentifier());
        lexicalElement = lexicalAnalyzer.peek();
        boolean needClosing = false;
        if (lexicalElement != null && lexicalElement.isSymbol("(")) {
            lexicalAnalyzer.get();
            needClosing = true;
            lexicalElement = lexicalAnalyzer.peek();
        }
        if ((needClosing && lexicalElement != null && !lexicalElement
                .isSymbol(")"))
                || ((!needClosing) && lexicalElement != null && !lexicalElement
                        .isSymbol("\n"))) {
            functionCall.setExpressionList(FactoryUtility
                    .getExpressionListAnalyzer(getFactory()).analyze());
        } else {
            functionCall.setExpressionList(null);
        }
        if (needClosing) {
            lexicalElement = lexicalAnalyzer.peek();
            if (lexicalElement != null && lexicalElement.isSymbol(")")) {
                lexicalAnalyzer.get();
            } else {
                throw new GenericSyntaxException(
                        "The closing ) is missing after the CALL statement");
            }
        }
        consumeEndOfLine();
        node.setFunctionCall(functionCall);
        return node;
    }
    @Override
    protected String getName() {
        return "CALL";
    }
}
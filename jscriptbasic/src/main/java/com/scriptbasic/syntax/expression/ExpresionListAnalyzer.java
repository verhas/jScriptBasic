package com.scriptbasic.syntax.expression;

import static com.scriptbasic.syntax.expression.LexFacade.get;
import static com.scriptbasic.syntax.expression.LexFacade.peek;

import com.scriptbasic.executors.GenericExpressionList;
import com.scriptbasic.interfaces.Expression;
import com.scriptbasic.interfaces.ExpressionList;
import com.scriptbasic.interfaces.LexicalAnalyzer;
import com.scriptbasic.interfaces.LexicalElement;
import com.scriptbasic.interfaces.SyntaxException;
import com.scriptbasic.syntax.AbstractAnalyzer;

public class ExpresionListAnalyzer extends AbstractAnalyzer {

    private final TagAnalyzer tagAnalyzer;

    public ExpresionListAnalyzer(final TagAnalyzer tagAnalyzer) {
        this.tagAnalyzer = tagAnalyzer;
    }

    @Override
    public ExpressionList analyze() throws SyntaxException {
        final GenericExpressionList expressionList = new GenericExpressionList();
        Expression expression = tagAnalyzer.getExpressionAnalyzer().analyze();
        expressionList.add(expression);
        LexicalElement lexicalElement = peek(getLexicalAnalyzer());
        while (isComma(lexicalElement)) {
            get(getLexicalAnalyzer());
            expression = tagAnalyzer.getExpressionAnalyzer().analyze();
            expressionList.add(expression);
            lexicalElement = peek(getLexicalAnalyzer());
        }
        return null;
    }

    private LexicalAnalyzer getLexicalAnalyzer() {
        return tagAnalyzer.getSyntaxAnalyzer().getLexicalAnalyzer();
    }

    private boolean isComma(final LexicalElement lexicalElement) {
        return lexicalElement.isSymbol() && ",".equals(lexicalElement.get());
    }

}

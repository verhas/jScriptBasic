package com.scriptbasic.syntax.expression;

import static com.scriptbasic.syntax.expression.LexFacade.get;
import static com.scriptbasic.syntax.expression.LexFacade.peek;
import static com.scriptbasic.utility.FactoryUtilities.getExpressionAnalyzer;
import static com.scriptbasic.utility.FactoryUtilities.getLexicalAnalyzer;

import com.scriptbasic.executors.GenericExpressionList;
import com.scriptbasic.interfaces.Expression;
import com.scriptbasic.interfaces.ExpressionList;
import com.scriptbasic.interfaces.ExpressionListAnalyzer;
import com.scriptbasic.interfaces.LexicalElement;
import com.scriptbasic.interfaces.SyntaxException;
import com.scriptbasic.syntax.AbstractAnalyzer;

public class BasicExpressionListAnalyzer extends AbstractAnalyzer implements
        ExpressionListAnalyzer {

    private BasicExpressionListAnalyzer() {
    }

    @Override
    public ExpressionList analyze() throws SyntaxException {
        final GenericExpressionList expressionList = new GenericExpressionList();
        Expression expression = getExpressionAnalyzer().analyze();
        expressionList.add(expression);
        LexicalElement lexicalElement = peek(getLexicalAnalyzer());
        while (isComma(lexicalElement)) {
            get(getLexicalAnalyzer());
            expression = getExpressionAnalyzer().analyze();
            expressionList.add(expression);
            lexicalElement = peek(getLexicalAnalyzer());
        }
        return expressionList;
    }

    private boolean isComma(final LexicalElement lexicalElement) {
        return lexicalElement != null && lexicalElement.isSymbol()
                && ",".equals(lexicalElement.get());
    }

}

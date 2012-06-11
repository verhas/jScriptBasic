package com.scriptbasic.syntax.expression;

import static com.scriptbasic.syntax.expression.LexFacade.get;
import static com.scriptbasic.syntax.expression.LexFacade.peek;

import com.scriptbasic.executors.GenericExpressionList;
import com.scriptbasic.interfaces.Expression;
import com.scriptbasic.interfaces.ExpressionList;
import com.scriptbasic.interfaces.ExpressionListAnalyzer;
import com.scriptbasic.interfaces.Factory;
import com.scriptbasic.interfaces.LexicalElement;
import com.scriptbasic.interfaces.SyntaxException;
import com.scriptbasic.syntax.AbstractAnalyzer;
import com.scriptbasic.utility.FactoryUtilities;

public class BasicExpressionListAnalyzer extends AbstractAnalyzer implements
        ExpressionListAnalyzer {
    private Factory factory;

    public Factory getFactory() {
        return factory;
    }

    public void setFactory(Factory factory) {
        this.factory = factory;
    }

    private BasicExpressionListAnalyzer() {
    }

    @Override
    public ExpressionList analyze() throws SyntaxException {
        final GenericExpressionList expressionList = new GenericExpressionList();
        Expression expression = FactoryUtilities.getExpressionAnalyzer(factory).analyze();
        expressionList.add(expression);
        LexicalElement lexicalElement = peek(FactoryUtilities.getLexicalAnalyzer(factory));
        while (isComma(lexicalElement)) {
            get(FactoryUtilities.getLexicalAnalyzer(factory));
            expression = FactoryUtilities.getExpressionAnalyzer(factory).analyze();
            expressionList.add(expression);
            lexicalElement = peek(FactoryUtilities.getLexicalAnalyzer(factory));
        }
        return expressionList;
    }

    private boolean isComma(final LexicalElement lexicalElement) {
        return lexicalElement != null && lexicalElement.isSymbol()
                && ",".equals(lexicalElement.get());
    }

}

package com.scriptbasic.interfaces;

public interface ExpressionAnalyzer extends FactoryManaged {
    public Expression analyze() throws SyntaxException;
}

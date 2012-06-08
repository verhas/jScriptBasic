package com.scriptbasic.interfaces;

public interface TagAnalyzer extends FactoryManaged {
    public Expression analyze() throws SyntaxException;
}

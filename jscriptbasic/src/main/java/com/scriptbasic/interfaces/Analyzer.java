package com.scriptbasic.interfaces;

public interface Analyzer {
    public AnalysisResult analyze() throws SyntaxException, LexicalException;
}

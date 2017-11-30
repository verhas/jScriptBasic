package com.scriptbasic.interfaces;


public interface ExpressionAnalyzer extends Analyzer<Expression> {
    @Override
    Expression analyze() throws AnalysisException;
}

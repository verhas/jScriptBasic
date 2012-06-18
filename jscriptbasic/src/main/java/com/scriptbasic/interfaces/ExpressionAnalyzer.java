package com.scriptbasic.interfaces;


public interface ExpressionAnalyzer extends FactoryManaged,
		Analyzer<Expression> {
	@Override
	Expression analyze() throws AnalysisException;
}

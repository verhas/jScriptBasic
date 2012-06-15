package com.scriptbasic.interfaces;

import com.scriptbasic.exceptions.AnalysisException;

public interface ExpressionAnalyzer extends FactoryManaged,
		Analyzer<Expression> {
	@Override
	Expression analyze() throws AnalysisException;
}

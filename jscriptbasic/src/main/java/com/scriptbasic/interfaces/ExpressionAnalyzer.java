package com.scriptbasic.interfaces;

import com.scriptbasic.exceptions.AnalysisException;

public interface ExpressionAnalyzer extends FactoryManaged,
		Analyzer<Expression> {
	@Override
	public Expression analyze() throws AnalysisException;
}

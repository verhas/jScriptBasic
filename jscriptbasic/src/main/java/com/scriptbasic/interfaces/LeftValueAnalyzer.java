package com.scriptbasic.interfaces;

import com.scriptbasic.exceptions.AnalysisException;

public interface LeftValueAnalyzer extends FactoryManaged, Analyzer<LeftValue> {
	@Override
	LeftValue analyze() throws AnalysisException;
}

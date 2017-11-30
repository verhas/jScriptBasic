package com.scriptbasic.interfaces;


public interface LeftValueAnalyzer extends Analyzer<LeftValue> {
	@Override
	LeftValue analyze() throws AnalysisException;
}

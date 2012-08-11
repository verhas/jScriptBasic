package com.scriptbasic.interfaces;
public interface LeftValueAnalyzer extends FactoryManaged, Analyzer<LeftValue> {
	@Override
	LeftValue analyze() throws AnalysisException;
}
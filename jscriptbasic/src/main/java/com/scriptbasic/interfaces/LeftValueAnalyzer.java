package com.scriptbasic.interfaces;

import com.scriptbasic.exceptions.AnalysisException;

public interface LeftValueAnalyzer extends FactoryManaged, Analyzer<LeftValue> {
	public LeftValue analyze() throws AnalysisException;
}

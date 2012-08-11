package com.scriptbasic.syntax;
import com.scriptbasic.interfaces.AnalysisResult;
import com.scriptbasic.interfaces.Analyzer;
import com.scriptbasic.interfaces.Factory;
public abstract class AbstractAnalyzer<T extends AnalysisResult> implements Analyzer<T> {
	public abstract Factory getFactory();
}
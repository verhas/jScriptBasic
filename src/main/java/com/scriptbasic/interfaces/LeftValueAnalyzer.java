package com.scriptbasic.interfaces;


import com.scriptbasic.api.LeftValue;

public interface LeftValueAnalyzer extends Analyzer<LeftValue> {
    @Override
    LeftValue analyze() throws AnalysisException;
}

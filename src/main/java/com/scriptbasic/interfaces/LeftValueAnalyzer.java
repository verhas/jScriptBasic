package com.scriptbasic.interfaces;


import com.scriptbasic.spi.LeftValue;

public interface LeftValueAnalyzer extends Analyzer<LeftValue> {
    @Override
    LeftValue analyze() throws AnalysisException;
}

package com.scriptbasic.interfaces;

import com.scriptbasic.exceptions.AnalysisException;

public interface Analyzer <T extends AnalysisResult>{
    public T analyze() throws AnalysisException;
}

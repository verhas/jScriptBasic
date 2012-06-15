package com.scriptbasic.interfaces;

import com.scriptbasic.exceptions.AnalysisException;

public interface TagAnalyzer extends FactoryManaged {
    Expression analyze() throws AnalysisException;
}

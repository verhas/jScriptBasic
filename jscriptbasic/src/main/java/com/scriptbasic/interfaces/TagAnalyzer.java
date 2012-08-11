package com.scriptbasic.interfaces;
public interface TagAnalyzer extends FactoryManaged {
    Expression analyze() throws AnalysisException;
}
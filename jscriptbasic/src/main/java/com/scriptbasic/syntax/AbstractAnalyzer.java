package com.scriptbasic.syntax;

import com.scriptbasic.interfaces.Analyzer;
import com.scriptbasic.interfaces.Factory;

public abstract class AbstractAnalyzer implements Analyzer {
    protected Factory factory;

    public Factory getFactory() {
        return factory;
    }
}

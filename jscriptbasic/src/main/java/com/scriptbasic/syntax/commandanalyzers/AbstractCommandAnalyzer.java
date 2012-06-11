package com.scriptbasic.syntax.commandanalyzers;

import com.scriptbasic.interfaces.CommandAnalyzer;
import com.scriptbasic.interfaces.Factory;
import com.scriptbasic.syntax.AbstractAnalyzer;

public abstract class AbstractCommandAnalyzer extends AbstractAnalyzer
        implements CommandAnalyzer {
    protected Factory factory;

    public Factory getFactory() {
        return factory;
    }

    public void setFactory(Factory factory) {
        this.factory = factory;
    }
}

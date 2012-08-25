package com.scriptbasic.syntax.leftvalue;

import com.scriptbasic.executors.GenericLeftValueList;
import com.scriptbasic.interfaces.AnalysisException;
import com.scriptbasic.interfaces.Factory;
import com.scriptbasic.interfaces.LeftValue;
import com.scriptbasic.interfaces.LeftValueAnalyzer;
import com.scriptbasic.interfaces.LeftValueList;
import com.scriptbasic.interfaces.LeftValueListAnalyzer;
import com.scriptbasic.syntax.AbstractGenericListAnalyzer;
import com.scriptbasic.utility.FactoryUtility;

public final class BasicLeftValueListAnalyzer
        extends
        AbstractGenericListAnalyzer<LeftValueList, GenericLeftValueList, LeftValue, LeftValueAnalyzer>
        implements LeftValueListAnalyzer {
    private Factory factory;

    @Override
    public Factory getFactory() {
        return factory;
    }

    @Override
    public void setFactory(final Factory factory) {
        this.factory = factory;
    }

    @Override
    public LeftValueList analyze() throws AnalysisException {
        return super.analyze(new GenericLeftValueList(),
                FactoryUtility.getLeftValueAnalyzer(getFactory()));
    }

}

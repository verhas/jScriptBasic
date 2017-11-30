package com.scriptbasic.syntax.leftvalue;

import com.scriptbasic.executors.GenericLeftValueList;
import com.scriptbasic.factories.Context;
import com.scriptbasic.interfaces.*;
import com.scriptbasic.syntax.AbstractGenericListAnalyzer;
import com.scriptbasic.utility.FactoryUtility;

public final class BasicLeftValueListAnalyzer
        extends
        AbstractGenericListAnalyzer<LeftValueList, GenericLeftValueList, LeftValue, LeftValueAnalyzer>
        implements LeftValueListAnalyzer {

    public BasicLeftValueListAnalyzer(Context ctx) {
        super(ctx);
    }

    @Override
    public LeftValueList analyze() throws AnalysisException {
        return super.analyze(new GenericLeftValueList(),ctx.leftValueAnalyzer);
    }

}

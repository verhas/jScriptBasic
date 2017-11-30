package com.scriptbasic.syntax.leftvalue;

import com.scriptbasic.executors.GenericLeftValueList;
import com.scriptbasic.factories.Context;
import com.scriptbasic.interfaces.*;
import com.scriptbasic.syntax.AbstractGenericListAnalyzer;
import com.scriptbasic.utility.FactoryUtility;

public final class BasicSimpleLeftValueListAnalyzer
        extends
        AbstractGenericListAnalyzer<LeftValueList, GenericLeftValueList, LeftValue, SimpleLeftValueAnalyzer>
        implements SimpleLeftValueListAnalyzer {

    public BasicSimpleLeftValueListAnalyzer(Context ctx) {
        super(ctx);
    }

    @Override
    public LeftValueList analyze() throws AnalysisException {
        return super.analyze(new GenericLeftValueList(),ctx.simpleLeftValueAnalyzer);
    }

}

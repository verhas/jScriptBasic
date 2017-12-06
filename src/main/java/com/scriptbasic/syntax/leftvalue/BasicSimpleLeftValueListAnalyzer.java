package com.scriptbasic.syntax.leftvalue;

import com.scriptbasic.executors.GenericLeftValueList;
import com.scriptbasic.context.Context;
import com.scriptbasic.interfaces.*;
import com.scriptbasic.syntax.AbstractGenericListAnalyzer;

public final class BasicSimpleLeftValueListAnalyzer
        extends
        AbstractGenericListAnalyzer<LeftValueList, GenericLeftValueList, LeftValue, SimpleLeftValueAnalyzer>
        implements SimpleLeftValueListAnalyzer {

    public BasicSimpleLeftValueListAnalyzer(final Context ctx) {
        super(ctx);
    }

    @Override
    public LeftValueList analyze() throws AnalysisException {
        return super.analyze(new GenericLeftValueList(), ctx.simpleLeftValueAnalyzer);
    }

}

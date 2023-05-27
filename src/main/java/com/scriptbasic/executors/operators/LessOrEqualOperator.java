package com.scriptbasic.executors.operators;

import com.scriptbasic.context.CompilerContext;

public class LessOrEqualOperator extends AbstractCompareOperator {

    @Override
    protected String operatorToJava(final CompilerContext cc) {
        return "<=";
    }

    @Override
    protected boolean decide(int comparisonResult) {
        return comparisonResult <= 0;
    }
}

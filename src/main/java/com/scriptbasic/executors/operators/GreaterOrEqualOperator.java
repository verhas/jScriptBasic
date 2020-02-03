package com.scriptbasic.executors.operators;

public class GreaterOrEqualOperator extends AbstractCompareOperator {

    @Override
    protected boolean decide(int comparisonResult) {
        return comparisonResult >= 0;
    }
}

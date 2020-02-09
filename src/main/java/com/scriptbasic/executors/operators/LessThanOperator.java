package com.scriptbasic.executors.operators;

public class LessThanOperator extends AbstractCompareOperator {

    @Override
    protected boolean decide(int comparisonResult) {
        return comparisonResult < 0;
    }
}

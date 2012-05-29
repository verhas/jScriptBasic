package com.scriptbasic.syntax.expression;

import java.util.Map;

import com.scriptbasic.command.executors.AbstractOperator;

public class BasicExpressionAnalyzer extends AbstractExpressionAnalyzer {

    @Override
    protected Integer getMaximumPriority() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    protected TagAnalyzer getTagAnalyzer() {
        // TODO Auto-generated method stub
        return null;
    }

    private Map<String, Class<? extends AbstractOperator>>[] operatorMapArray = new Map<String, Class<? extends AbstractOperator>>[getMaximumPriority()];

    @Override
    protected Map<String, Class<? extends AbstractOperator>> getOperatorMap(
            Integer priority) {
        // TODO Auto-generated method stub
        return null;
    }

}

package com.scriptbasic.executors;

public abstract class AbstractIdentifieredExpression extends AbstractExpression {
    private String variableName;

    public String getVariableName() {
        return variableName;
    }

    public void setVariableName(final String variableName) {
        this.variableName = variableName;
    }

}

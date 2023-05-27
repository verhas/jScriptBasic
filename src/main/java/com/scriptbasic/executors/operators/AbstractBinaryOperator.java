package com.scriptbasic.executors.operators;

import com.scriptbasic.context.CompilerContext;
import com.scriptbasic.interfaces.Expression;

public abstract class AbstractBinaryOperator extends AbstractOperator {
    
    @Override
    public String toJava(CompilerContext cc){
        return getLeftOperand().toJava(cc) + operatorToJava(cc) + getRightOperand().toJava(cc);
    }

    protected abstract String operatorToJava(final CompilerContext cc);

    private Expression leftOperand;
    private Expression rightOperand;

    public Expression getLeftOperand() {
        return this.leftOperand;
    }

    public void setLeftOperand(final Expression leftOperand) {
        this.leftOperand = leftOperand;
    }

    public Expression getRightOperand() {
        return this.rightOperand;
    }

    public void setRightOperand(final Expression rightOperand) {
        this.rightOperand = rightOperand;
    }

}

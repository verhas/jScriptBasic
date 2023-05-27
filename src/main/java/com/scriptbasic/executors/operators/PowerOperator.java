package com.scriptbasic.executors.operators;

import com.scriptbasic.context.CompilerContext;
import com.scriptbasic.executors.rightvalues.BasicDoubleValue;
import com.scriptbasic.spi.RightValue;

public class PowerOperator extends AbstractBinaryFullCircuitFullDoubleOperator {


    @Override
    protected String operatorToJava(final CompilerContext cc) {
        return null;
    }

    @Override
    public String toJava(CompilerContext cc) {
        return "java.lang.Math.pow(" + getLeftOperand().toJava(cc) + "," + getRightOperand().toJava(cc) + ")";
    }

    @Override
    protected RightValue operateOnDoubleDouble(final Double a, final Double b) {
        return new BasicDoubleValue(Math.pow(a, b));
    }

    @Override
    protected String operatorName() {
        return "Power";
    }

}

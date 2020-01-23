package com.scriptbasic.executors.rightvalues;

import com.scriptbasic.executors.operators.AbstractBinaryOperator;
import com.scriptbasic.interfaces.BasicRuntimeException;
import com.scriptbasic.spi.RightValue;

abstract public class AbstractJavaObjectWithOperators extends BasicJavaObjectValue {

    public AbstractJavaObjectWithOperators(Object value) {
        super(value);
    }

    public abstract RightValue evaluateBinaryOperatorFromLeft(AbstractBinaryOperator binaryOperator,
                                                                 RightValue rightOperand) throws BasicRuntimeException;

    public abstract RightValue evaluateBinaryOperatorFromRight(AbstractBinaryOperator binaryOperator,
                                                                  RightValue leftOperand) throws BasicRuntimeException;

}

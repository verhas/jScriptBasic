package com.scriptbasic.executors.operators;

import com.scriptbasic.executors.AbstractNumericRightValue;
import com.scriptbasic.interfaces.BasicRuntimeException;
import com.scriptbasic.interfaces.RightValue;

public abstract class AbstractBinaryFullCircuitNumericOperator<T extends Number>
        extends AbstractBinaryFullCircuitOperator {

    protected abstract RightValue operateOnDoubleDouble(Double a, Double b)
            throws BasicRuntimeException;

    protected abstract RightValue operateOnDoubleLong(Double a, Long b)
            throws BasicRuntimeException;

    protected abstract RightValue operateOnLongDouble(Long a, Double b)
            throws BasicRuntimeException;

    protected abstract RightValue operateOnLongLong(Long a, Long b)
            throws BasicRuntimeException;

    protected abstract String operatorName();

    @SuppressWarnings("unchecked")
    @Override
    protected final RightValue evaluateOnValues(RightValue leftOperand,
            RightValue rightOperand) throws BasicRuntimeException {
        Number a, b;
        RightValue result = null;
        if (leftOperand.isNumeric() && rightOperand.isNumeric()) {
            a = ((AbstractNumericRightValue<Number>) leftOperand).getValue();
            b = ((AbstractNumericRightValue<Number>) rightOperand).getValue();
            if (leftOperand.isDouble()) {
                if (rightOperand.isDouble()) {
                    result = operateOnDoubleDouble((Double) a, (Double) b);
                } else {
                    result = operateOnDoubleLong((Double) a, (Long) b);
                }
            } else {
                if (rightOperand.isDouble()) {
                    result = operateOnLongDouble((Long) a, (Double) b);
                } else {
                    result = operateOnLongLong((Long) a, (Long) b);
                }
            }
            return result;
        } else {
            throw new BasicRuntimeException(operatorName()
                    + " operator applied to non numeric argument");
        }
    }

}

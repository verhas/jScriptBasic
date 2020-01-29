package com.scriptbasic.executors.operators;

import com.scriptbasic.executors.rightvalues.AbstractNumericRightValue;
import com.scriptbasic.interfaces.BasicRuntimeException;
import com.scriptbasic.spi.RightValue;

public abstract class AbstractBinaryFullCircuitNumericOperator<T extends Number>
        extends AbstractBinaryFullCircuitOperator {

    protected abstract RightValue operateOnDoubleDouble(Double a, Double b)
            ;

    protected abstract RightValue operateOnDoubleLong(Double a, Long b)
        ;

    protected abstract RightValue operateOnLongDouble(Long a, Double b)
        ;

    protected abstract RightValue operateOnLongLong(Long a, Long b)
        ;
    
    protected abstract RightValue operateOnDate(Long a, Long b);

    protected RightValue operateOnValues(final RightValue leftOperand,
                                         final RightValue rightOperand) throws BasicRuntimeException {
        throw new BasicRuntimeException(operatorName()
                + " operator applied to non numeric argument");
    }

    protected abstract String operatorName();

    @SuppressWarnings("unchecked")
    @Override
    protected final RightValue evaluateOn(final RightValue leftOperand,
                                          final RightValue rightOperand) throws BasicRuntimeException {
        final Number a;
        final Number b;
        final RightValue result;
        if (leftOperand == null || rightOperand == null) {
            throw new BasicRuntimeException(
                    "Can not execute the operation on undefined value.");
        }
        if (leftOperand.isNumeric() && rightOperand.isNumeric()) {
            a = ((AbstractNumericRightValue<Number, Object>) leftOperand).getNumericValue();
            b = ((AbstractNumericRightValue<Number, Object>) rightOperand).getNumericValue();
            if (leftOperand.isDouble()) {
                if (rightOperand.isDouble()) {
                    result = operateOnDoubleDouble((Double) a, (Double) b);
                } else if(rightOperand.isDate()) {
                    throw new BasicRuntimeException(
                            "Can not execute the operation on double value.");                    
                } else {
                    result = operateOnDoubleLong((Double) a, (Long) b);
                }
            } else if (leftOperand.isDate()) {
                if(rightOperand.isLong()) {
                    result = operateOnDate((Long) a, (Long) b);
                } else {
                    throw new BasicRuntimeException(
                            "Can not execute the operation on double value.");                    
                }
            } else {
                if (rightOperand.isDouble()) {
                    result = operateOnLongDouble((Long) a, (Double) b);
                } else
                if (rightOperand.isDate()) {
                    result = operateOnDate((Long) a, (Long) b);
                } else {
                    result = operateOnLongLong((Long) a, (Long) b);
                }
            }
            return result;
        } else {
            return operateOnValues(leftOperand, rightOperand);
        }
    }

}

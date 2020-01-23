package com.scriptbasic.executors.rightvalues;

import java.time.LocalDate;

import com.scriptbasic.executors.operators.AbstractBinaryOperator;
import com.scriptbasic.executors.operators.AddOperator;
import com.scriptbasic.executors.operators.MinusOperator;
import com.scriptbasic.interfaces.BasicRuntimeException;
import com.scriptbasic.spi.RightValue;

public class BasicDateValue extends AbstractJavaObjectWithOperators
    implements Comparable<BasicDateValue> {

    public BasicDateValue(LocalDate localDate) {
        super(localDate);
    }
    
    public LocalDate getLocalDate() {
        return (LocalDate)getValue();
    }

    @Override
    public RightValue evaluateBinaryOperatorFromLeft(AbstractBinaryOperator binaryOperator, RightValue rightOperand) 
            throws BasicRuntimeException {
        if(binaryOperator instanceof MinusOperator) {
            return minus(rightOperand);
        } else
        if(binaryOperator instanceof AddOperator) {
            return add(rightOperand);
        }
        throw new BasicRuntimeException("Class BasicDateValue is not supporting operator: "+binaryOperator.toString());
    }

    private RightValue add(RightValue operand) throws BasicRuntimeException {
        if(operand.isLong()) {
            AbstractNumericRightValue<Number> numOperand = (AbstractNumericRightValue<Number>)operand;
            long l = numOperand.getValue().longValue();
            
            LocalDate result = getLocalDate().plusDays(l);
            return new BasicDateValue(result);            
        }
        throw new BasicRuntimeException("Unsupported operand: "+operand.toString());
    }

    private RightValue minus(RightValue rightOperand) throws BasicRuntimeException {
        if(rightOperand.isLong()) {
            AbstractNumericRightValue<Number> numOperand = (AbstractNumericRightValue<Number>)rightOperand;
            long l = numOperand.getValue().longValue();
            
            LocalDate result = getLocalDate().minusDays(l);
            return new BasicDateValue(result);
        }
        throw new BasicRuntimeException("Unsupported operand: "+rightOperand.toString());
    }

    @Override
    public RightValue evaluateBinaryOperatorFromRight(AbstractBinaryOperator binaryOperator, RightValue leftOperand) 
            throws BasicRuntimeException {
        if(binaryOperator instanceof AddOperator) {
            return add(leftOperand);
        }
        throw new BasicRuntimeException("Class BasicDateValue is not supporting operator: "+binaryOperator.toString());
    }

    @Override
    public int compareTo(BasicDateValue o) {
        if(o==null) {
            return 1;
        }
        LocalDate localDate = getLocalDate();
        LocalDate otherLocalDate = o.getLocalDate();
        return localDate.compareTo(otherLocalDate);
    }

}

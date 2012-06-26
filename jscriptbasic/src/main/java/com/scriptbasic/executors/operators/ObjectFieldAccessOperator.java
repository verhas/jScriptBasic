package com.scriptbasic.executors.operators;

import java.lang.reflect.Field;

import com.scriptbasic.executors.rightvalues.AbstractPrimitiveRightValue;
import com.scriptbasic.executors.rightvalues.VariableAccess;
import com.scriptbasic.interfaces.BasicRuntimeException;
import com.scriptbasic.interfaces.ExecutionException;
import com.scriptbasic.interfaces.Expression;
import com.scriptbasic.interfaces.ExtendedInterpreter;
import com.scriptbasic.interfaces.RightValue;
import com.scriptbasic.utility.RightValueUtils;

/**
 * This is the highest priority operator (priority 1) that is used to access a
 * field of an object. This operator is the (.) dot operator.
 * 
 * @author Peter Verhas
 * 
 */
public class ObjectFieldAccessOperator extends AbstractBinaryOperator {

    @Override
    public RightValue evaluate(ExtendedInterpreter extendedInterpreter)
            throws ExecutionException {
        RightValue result = null;
        RightValue leftOp = getLeftOperand().evaluate(extendedInterpreter);
        if (!(leftOp instanceof AbstractPrimitiveRightValue<?>)) {
            throw new BasicRuntimeException("Can not get field access from "
                    + leftOp.getClass());
        }
        @SuppressWarnings("unchecked")
        Object object = ((AbstractPrimitiveRightValue<Object>) leftOp)
                .getValue();
        Expression rightOp = getRightOperand();
        if (rightOp instanceof VariableAccess) {
            String fieldName = ((VariableAccess) rightOp).getVariableName();
            Class<?> klass = object.getClass();
            try {
                Field field = klass.getField(fieldName);
                result = RightValueUtils.createRightValue(field.get(object));
            } catch (NoSuchFieldException | SecurityException
                    | IllegalArgumentException | IllegalAccessException e) {
                throw new BasicRuntimeException("Object access of type "
                        + object.getClass() + " can not access field '"
                        + fieldName + "'");
            }
        } else {
            throw new BasicRuntimeException(
                    "Field access operator is not implemented to handle variable field.");
        }
        return result;
    }
}

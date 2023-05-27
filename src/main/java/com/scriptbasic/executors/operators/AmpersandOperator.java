package com.scriptbasic.executors.operators;

import com.scriptbasic.api.ScriptBasicException;
import com.scriptbasic.context.CompilerContext;
import com.scriptbasic.executors.rightvalues.BasicStringValue;
import com.scriptbasic.spi.RightValue;

/**
 * Generates a string concatenation of two expressions.
 * 
 * If the data type of expression1 or expression2 is not String but widens to String, 
 * it is converted to String. If either of the data types does not widen to String, 
 * the compiler generates an error.
 * 
 * The data type of result is String. If one or both expressions evaluate to Nothing 
 * or have null value, they are treated as a string with a value of "".
 */
public class AmpersandOperator extends AbstractBinaryFullCircuitOperator {

    @Override
    protected RightValue evaluateOn(RightValue leftOperand, RightValue rightOperand) throws ScriptBasicException {
        
        StringBuilder sb = new StringBuilder();
        addOperand(sb, leftOperand);
        addOperand(sb, rightOperand);

        return new BasicStringValue(sb.toString());
    }

    @Override
    protected String operatorToJava(final CompilerContext cc) {
        return "+";
    }

    static private void addOperand(StringBuilder stringBuilder, RightValue operand) throws ScriptBasicException {
        if(operand==null) {
            return;
        }
        stringBuilder.append(BasicStringValue.asString(operand));
    }

}

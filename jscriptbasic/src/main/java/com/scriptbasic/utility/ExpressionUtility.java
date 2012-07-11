/**
 * 
 */
package com.scriptbasic.utility;

import com.scriptbasic.exceptions.GenericSyntaxException;
import com.scriptbasic.executors.operators.ObjectFieldAccessOperator;
import com.scriptbasic.executors.rightvalues.BasicStringValue;
import com.scriptbasic.executors.rightvalues.VariableAccess;
import com.scriptbasic.interfaces.AnalysisException;
import com.scriptbasic.interfaces.Expression;

/**
 * @author Peter Verhas
 * @date June 28, 2012
 * 
 */
public final class ExpressionUtility {
    private ExpressionUtility() {
        UtilityUtility.throwExceptionToEnsureNobodyCallsIt();
    }

    /**
     * Convert an expression of the form 'a.b.c.d' into a String.
     * 
     * @param expression
     * @return the string containing the dots and the identifiers
     * @throws AnalysisException
     *             when the expression does not match the format
     * 
     */
    public static String convertToString(Expression expression)
            throws AnalysisException {
        if (expression instanceof VariableAccess) {
            return ((VariableAccess) expression).getVariableName();
        }
        if (expression instanceof ObjectFieldAccessOperator) {
            ObjectFieldAccessOperator ofao = (ObjectFieldAccessOperator) expression;
            return convertToString(ofao.getLeftOperand()) + "."
                    + convertToString(ofao.getRightOperand());
        }
        if (expression instanceof BasicStringValue) {
            return ((BasicStringValue) expression).getValue();
        }
        throw new GenericSyntaxException(
                "class, package or symbol name are not vaid in command USE");
    }
}

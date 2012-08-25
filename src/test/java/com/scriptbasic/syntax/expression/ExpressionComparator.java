package com.scriptbasic.syntax.expression;

import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;

import java.util.Iterator;

import com.scriptbasic.executors.AbstractIdentifieredExpression;
import com.scriptbasic.executors.AbstractIdentifieredExpressionListedExpression;
import com.scriptbasic.executors.operators.AbstractBinaryOperator;
import com.scriptbasic.executors.operators.AbstractUnaryOperator;
import com.scriptbasic.executors.rightvalues.AbstractPrimitiveRightValue;
import com.scriptbasic.interfaces.Expression;
import com.scriptbasic.interfaces.ExpressionList;

public class ExpressionComparator {

    private static void assertEqual(final AbstractBinaryOperator a,
            final AbstractBinaryOperator b) {
        assertEqual(a.getLeftOperand(), b.getLeftOperand());
        assertEqual(a.getRightOperand(), b.getRightOperand());
    }

    private static void assertEqual(final AbstractUnaryOperator a,
            final AbstractUnaryOperator b) {
        assertEqual(a.getOperand(), b.getOperand());
    }

    public static void assertEqual(final ExpressionList a,
            final ExpressionList b) {
        if (a == null && b == null) {
            return;
        }
        final Iterator<Expression> itea = a.iterator();
        final Iterator<Expression> iteb = b.iterator();

        while (itea.hasNext() && iteb.hasNext()) {
            assertEqual(itea.next(), iteb.next());
        }
        assertFalse(itea.hasNext() || iteb.hasNext());
    }

    private static void assertEqual(
            final AbstractIdentifieredExpressionListedExpression a,
            final AbstractIdentifieredExpressionListedExpression b) {
        if (a.getVariableName() == null && b.getVariableName() == null) {
            return;
        }
        assertTrue(a.getVariableName() + "!=" + b.getVariableName(), a
                .getVariableName().equals(b.getVariableName()));
        assertEqual(a.getExpressionList(), b.getExpressionList());
    }

    private static void assertEqual(final AbstractPrimitiveRightValue<?> a,
            final AbstractPrimitiveRightValue<?> b) {
        assertTrue(a.getValue() + "!=" + b.getValue(),
                a.getValue().equals(b.getValue()));
    }

    private static void assertEqual(final AbstractIdentifieredExpression a,
            final AbstractIdentifieredExpression b) {
        assertTrue(a.getVariableName().equals(b.getVariableName()));
    }

    public static void assertEqual(final Expression a, final Expression b) {

        if (a == null && b == null) {
            return;
        }

        assertTrue(a.getClass() + "!=" + b.getClass(),
                a.getClass().equals(b.getClass()));

        if (a instanceof AbstractPrimitiveRightValue<?>) {
            assertEqual((AbstractPrimitiveRightValue<?>) a,
                    (AbstractPrimitiveRightValue<?>) b);
            return;
        }

        if (a instanceof AbstractBinaryOperator) {
            assertEqual((AbstractBinaryOperator) a, (AbstractBinaryOperator) b);
            return;
        }
        if (a instanceof AbstractUnaryOperator) {
            assertEqual((AbstractUnaryOperator) a, (AbstractUnaryOperator) b);
            return;
        }
        if (a instanceof AbstractIdentifieredExpressionListedExpression) {
            assertEqual((AbstractIdentifieredExpressionListedExpression) a,
                    (AbstractIdentifieredExpressionListedExpression) b);
            return;
        }
        if (a instanceof AbstractIdentifieredExpression) {
            assertEqual((AbstractIdentifieredExpression) a,
                    (AbstractIdentifieredExpression) b);
            return;
        }
        assertTrue(false);
    }
}

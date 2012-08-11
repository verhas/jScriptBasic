package com.scriptbasic.syntax.expression;
import com.scriptbasic.executors.AbstractIdentifieredExpressionListedExpression;
import com.scriptbasic.executors.GenericExpressionList;
import com.scriptbasic.executors.operators.AbstractBinaryOperator;
import com.scriptbasic.executors.operators.AbstractUnaryOperator;
import com.scriptbasic.executors.operators.AddOperator;
import com.scriptbasic.executors.operators.MultiplyOperator;
import com.scriptbasic.executors.operators.ObjectFieldAccessOperator;
import com.scriptbasic.executors.operators.UnaryOperatorMinus;
import com.scriptbasic.executors.operators.UnaryOperatorNot;
import com.scriptbasic.executors.operators.UnaryOperatorPlus;
import com.scriptbasic.executors.rightvalues.ArrayElementAccess;
import com.scriptbasic.executors.rightvalues.BasicBooleanValue;
import com.scriptbasic.executors.rightvalues.BasicDoubleValue;
import com.scriptbasic.executors.rightvalues.BasicLongValue;
import com.scriptbasic.executors.rightvalues.BasicStringValue;
import com.scriptbasic.executors.rightvalues.FunctionCall;
import com.scriptbasic.executors.rightvalues.VariableAccess;
import com.scriptbasic.interfaces.Expression;
import com.scriptbasic.interfaces.ExpressionList;
public class ExpressionBuilder {
    public static Expression binaryOp(
            final Class<? extends AbstractBinaryOperator> klass,
            final Expression a, final Expression b) throws Exception {
        final AbstractBinaryOperator op = klass.newInstance();
        op.setLeftOperand(a);
        op.setRightOperand(b);
        return op;
    }
    public static Expression multiply(final Expression a, final Expression b)
            throws Exception {
        return binaryOp(MultiplyOperator.class, a, b);
    }
    public static Expression add(final Expression a, final Expression b)
            throws Exception {
        return binaryOp(AddOperator.class, a, b);
    }
    public static Expression unaryOp(
            final Class<? extends AbstractUnaryOperator> klass,
            final Expression a) throws Exception {
        final AbstractUnaryOperator op = klass.newInstance();
        op.setOperand(a);
        return op;
    }
    public static Expression not(final Expression a) throws Exception {
        return unaryOp(UnaryOperatorNot.class, a);
    }
    public static Expression unaryMinus(final Expression a) throws Exception {
        return unaryOp(UnaryOperatorMinus.class, a);
    }
    public static Expression unaryPlus(final Expression a) throws Exception {
        return unaryOp(UnaryOperatorPlus.class, a);
    }
    public static Expression variable(final String s) {
        final VariableAccess va = new VariableAccess();
        va.setVariableName(s);
        return va;
    }
    private static Expression funOrArray(
            final AbstractIdentifieredExpressionListedExpression foa,
            final String name, final Expression[] indices) {
        foa.setVariableName(name);
        final GenericExpressionList expressionList = indices == null ? null
                : new GenericExpressionList();
        if (indices != null) {
            for (final Expression e : indices) {
                expressionList.add(e);
            }
        }
        foa.setExpressionList(expressionList);
        return foa;
    }
    public static Expression func(final String name,
            final Expression... indices) {
        return funOrArray(new FunctionCall(), name, indices);
    }
    public static Expression array(final String name,
            final Expression... indices) {
        return funOrArray(new ArrayElementAccess(), name, indices);
    }
    public static ExpressionList LIST(Expression ...expressions ){
    	final GenericExpressionList expressionList =  new GenericExpressionList();
    	for( Expression expression : expressions ){
    		expressionList.add(expression);
    	}
    	return expressionList;
    }
    public static Expression BOOL(final Boolean b) {
        return new BasicBooleanValue(b);
    }
    public static Expression LONG(final Long l) {
        return new BasicLongValue(l);
    }
    public static Expression DOUBLE(final Double d) {
        return new BasicDoubleValue(d);
    }
    public static Expression STRING(final String s) {
        return new BasicStringValue(s);
    }
    public static Expression ID(final String s) {
        final VariableAccess va = new VariableAccess();
        va.setVariableName(s);
        return va;
    }
    public static Expression OBJECT_FIELD(final String a, final String b) {
        return OBJECT_FIELD(ID(a), ID(b));
    }
    public static Expression OBJECT_FIELD(final String a, final Expression b) {
        return OBJECT_FIELD(ID(a), b);
    }
    public static Expression OBJECT_FIELD(final Expression a, final String b) {
        return OBJECT_FIELD(a, ID(b));
    }
    public static Expression OBJECT_FIELD(final Expression a, final Expression b) {
        final ObjectFieldAccessOperator ofa = new ObjectFieldAccessOperator();
        ofa.setLeftOperand(a);
        ofa.setRightOperand(b);
        return ofa;
    }
}
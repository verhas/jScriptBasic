package com.scriptbasic.executors.commands;

import java.util.ArrayList;
import java.util.List;

import com.scriptbasic.api.ScriptBasicException;
import com.scriptbasic.executors.operators.RightSideEqualsOperator;
import com.scriptbasic.executors.operators.RightSideGreaterOrEqualOperator;
import com.scriptbasic.executors.operators.RightSideLessOrEqualOperator;
import com.scriptbasic.executors.rightvalues.AbstractPrimitiveRightValue;
import com.scriptbasic.executors.rightvalues.BasicBooleanValue;
import com.scriptbasic.interfaces.BasicRuntimeException;
import com.scriptbasic.interfaces.Expression;
import com.scriptbasic.spi.Interpreter;
import com.scriptbasic.spi.RightValue;

public class CommandCase
	extends AbstractCommandSelectPart
{
	public static interface CaseCondition {

		boolean matchCase(Interpreter interpreter, RightValue selectExpressionValue) throws ScriptBasicException;
		
	}
	
	public static class EqualCaseCondition implements CaseCondition {
		final Expression expression;
		
		public EqualCaseCondition(final Expression expression) {
			this.expression = expression;
		}

		@Override
		public boolean matchCase(Interpreter interpreter, RightValue selectExpressionValue) throws ScriptBasicException {
			RightSideEqualsOperator operator = new RightSideEqualsOperator(selectExpressionValue);			
			operator.setRightOperand(expression);
			RightValue conditionValue = operator.evaluate(interpreter);
	        if (conditionValue instanceof AbstractPrimitiveRightValue<?>) {
	            return BasicBooleanValue.asBoolean(conditionValue);
	        } else {
	            throw new BasicRuntimeException(
	                    "Case condition can not be evaluated to boolean");
	        }
		}
	}
	
	public static class FromToCaseCondition implements CaseCondition {
		final private Expression fromExpression;
		final private Expression toExpression;

		public FromToCaseCondition(final Expression fromExpression, final Expression toExpression)
		{
			this.fromExpression = fromExpression;
			this.toExpression = toExpression;
		}

		@Override
		public boolean matchCase(Interpreter interpreter, RightValue selectExpressionValue) 
				throws ScriptBasicException {
			RightSideGreaterOrEqualOperator leftOperator = new RightSideGreaterOrEqualOperator(selectExpressionValue);
			leftOperator.setRightOperand(fromExpression);
			RightValue conditionValue = leftOperator.evaluate(interpreter);
	        if (conditionValue instanceof AbstractPrimitiveRightValue<?>) {
	            if(!BasicBooleanValue.asBoolean(conditionValue)) {
	            	return false;
	            }
	            // evaluate to condition
	            RightSideLessOrEqualOperator rightOperator = new RightSideLessOrEqualOperator(selectExpressionValue);
	            rightOperator.setRightOperand(toExpression);
	            conditionValue = rightOperator.evaluate(interpreter);
		        if (conditionValue instanceof AbstractPrimitiveRightValue<?>) {
		        	return BasicBooleanValue.asBoolean(conditionValue);
		        }
	        }

	        throw new BasicRuntimeException("Case condition can not be evaluated to boolean");
		}
		
	}
	
	/**
	 * List of case conditions to be evaluated
	 */
	private List<CaseCondition> expressionList = new ArrayList<>();
	private CommandEndSelect commandEndSelect;

	@Override
	public void execute(Interpreter interpreter) throws ScriptBasicException {
		// run this command
		// Check if finished
		Boolean caseApplied = (Boolean) interpreter.getMap().get(CommandSelect.CASE_APPLIED);
		if(caseApplied) {
			// skip to end
			interpreter.setNextCommand(commandEndSelect);
		} else {
			// set applied
			interpreter.getMap().put(CommandSelect.CASE_APPLIED, Boolean.TRUE);
			
			interpreter.setNextCommand(getNextCommand());		
		}
		
	}

	public void addCaseCondition(CaseCondition caseCondition) {
		expressionList.add(caseCondition);
	}

	public boolean matchCase(Interpreter interpreter, RightValue expressionValue) throws ScriptBasicException {
		// default case
		if(expressionList.size()==0) {
			return true;
		}
		for(CaseCondition caseCondition: expressionList) {
			if(caseCondition.matchCase(interpreter, expressionValue)) {
				return true;
			}
		}
		return false;
	}

	public void addCaseEqualCondition(Expression expression) {
		EqualCaseCondition caseCondition = new EqualCaseCondition(expression); 
		addCaseCondition(caseCondition);
	}

	public void addCaseFromToCondition(Expression fromExpression, Expression toExpression) {
		FromToCaseCondition caseCondition = new FromToCaseCondition(fromExpression, toExpression); 
		addCaseCondition(caseCondition);
		
	}

	public void setEndSelect(CommandEndSelect commandEndSelect) {
		// TODO: Jak vyskocit ven z case casti?
		this.commandEndSelect = commandEndSelect;
	}

}

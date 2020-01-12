package com.scriptbasic.executors.commands;

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

import java.util.ArrayList;
import java.util.List;

public class CommandCase extends AbstractCommandSelectPart {

    /**
     * One condition in the case statement
     *
     * Single case statement can contain multiple conditions.
     */
	public interface CaseCondition {

		boolean matchCase(Interpreter interpreter, RightValue selectExpressionValue) throws ScriptBasicException;		
	}
	
	public static class EqualCaseCondition implements CaseCondition {
		final Expression expression;
		
		public EqualCaseCondition(final Expression expression) {
			this.expression = expression;
		}

		@Override
		public boolean matchCase(Interpreter interpreter, RightValue selectExpressionValue) throws ScriptBasicException {
			final var operator = new RightSideEqualsOperator(selectExpressionValue);			
			operator.setRightOperand(expression);
			final var conditionValue = operator.evaluate(interpreter);
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

		public FromToCaseCondition(final Expression fromExpression, final Expression toExpression) {
			this.fromExpression = fromExpression;
			this.toExpression = toExpression;
		}

		@Override
		public boolean matchCase(Interpreter interpreter, RightValue selectExpressionValue) 
				throws ScriptBasicException {
			final var leftOperator = new RightSideGreaterOrEqualOperator(selectExpressionValue);
			leftOperator.setRightOperand(fromExpression);
			var conditionValue = leftOperator.evaluate(interpreter);
	        if (conditionValue instanceof AbstractPrimitiveRightValue<?>) {
	            if(!BasicBooleanValue.asBoolean(conditionValue)) {
	            	return false;
	            }
	            // evaluate to condition
	            final var rightOperator = new RightSideLessOrEqualOperator(selectExpressionValue);
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
	private final List<CaseCondition> expressionList = new ArrayList<>();
	private CommandEndSelect commandEndSelect;

	@Override
	public void execute(Interpreter interpreter) {

		// Check if other case already applied
		Boolean caseApplied = (Boolean) interpreter.getMap().get(CommandSelect.CASE_APPLIED);
		if(caseApplied) {
			// skip to end
			interpreter.setNextCommand(commandEndSelect);
		} else {
			// set as applied and execute this case
		    
			interpreter.getMap().put(CommandSelect.CASE_APPLIED, true);
			
			interpreter.setNextCommand(getNextCommand());		
		}
		
	}

	public void addCaseCondition(final CaseCondition caseCondition) {
		expressionList.add(caseCondition);
	}

	public boolean matches(Interpreter interpreter, RightValue expressionValue) throws ScriptBasicException { 
	    // default case has empty list of expressions
		if(expressionList.size()==0) {
			return true;
		}
		
		// check case specific conditions
		for(final var caseCondition: expressionList) {
			if(caseCondition.matchCase(interpreter, expressionValue)) {
				return true;
			}
		}
		
		// any condition do not math 
		return false;
	}

	public void addCaseEqualCondition(Expression expression) {
		final var caseCondition = new EqualCaseCondition(expression); 
		addCaseCondition(caseCondition);
	}

	public void addCaseFromToCondition(Expression fromExpression, Expression toExpression) {
	    final var caseCondition = new FromToCaseCondition(fromExpression, toExpression); 
		addCaseCondition(caseCondition);
		
	}

	public void setEndSelect(CommandEndSelect commandEndSelect) {
		this.commandEndSelect = commandEndSelect;
	}

}

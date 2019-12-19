package com.scriptbasic.executors.commands;

import java.util.ArrayList;
import java.util.List;

import com.scriptbasic.api.ScriptBasicException;
import com.scriptbasic.interfaces.Expression;
import com.scriptbasic.spi.Interpreter;

public class CommandSelect extends AbstractCommandSelectPart {
	
	static public final String CASE_APPLIED = "SELECT_CASE_APPLIED";
	
	/**
	 * Expression in select statement
	 */
	private Expression expression;

	private CommandEndSelect commandEndSelect;
	
	/**
	 * List of possible cases
	 */
	private List<CommandCase> cases = new ArrayList<>();

	public void setExpression(final Expression expression) {
		this.expression = expression;		
	}	

	@Override
	public void execute(Interpreter interpreter) throws ScriptBasicException {
		final var expressionValue = expression.evaluate(interpreter);
		// Mark as not finished
		interpreter.getMap().put(CASE_APPLIED, false);
		
		// Iterate over all the cases and sets the next 
		// command pointer/index on the first matching case
		for(final var singleCase: cases) {
		  if(singleCase.matches(interpreter, expressionValue)) {
			  interpreter.setNextCommand(singleCase);
			  return;
		  }
		}
		// no case found -> continue with endSelect
		interpreter.setNextCommand(commandEndSelect.getNextCommand());
	}

	/**
	 * Set command for End Select statement
	 * 
	 * Method is called by parser at the end
	 * of select statement.
	 * 
	 * @param commandEndSelect command
	 */
	public void setEndSelectNode(CommandEndSelect commandEndSelect) {
		this.commandEndSelect = commandEndSelect;

		for(final var singleCase: cases) {
			singleCase.setEndSelect(commandEndSelect);
		}
	}

	public void registerCase(CommandCase node) {
		cases.add(node);		
	}

}

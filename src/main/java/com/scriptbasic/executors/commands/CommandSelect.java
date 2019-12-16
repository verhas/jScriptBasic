package com.scriptbasic.executors.commands;

import java.util.ArrayList;
import java.util.List;

import com.scriptbasic.api.ScriptBasicException;
import com.scriptbasic.interfaces.Expression;
import com.scriptbasic.spi.Interpreter;

public class CommandSelect extends AbstractCommandSelectPart {
	
	static public final String CASE_APPLIED = "SELECT_CASE_APPLIED";
	
	/**
	 * 
	 */
	private Expression expression;

	private CommandEndSelect commandEndSelect;
	
	private List<CommandCase> cases = new ArrayList<>();

	public void setExpression(final Expression expression) {
		this.expression = expression;		
	}	

	@Override
	public void execute(Interpreter interpreter) throws ScriptBasicException {
		// Evaluate expression
		final var expressionValue = expression.evaluate(interpreter);
		// Mark as not finished
		interpreter.getMap().put(CASE_APPLIED, Boolean.FALSE);
		
		// Iterate all cases
		for(var singleCase: cases) {
		  if(singleCase.matchCase(interpreter, expressionValue)) {
			  // run this case
			  interpreter.setNextCommand(singleCase);
			  return;
		  }
		}
		// no case found -> continue with endSelect
		interpreter.setNextCommand(commandEndSelect.getNextCommand());
	}

	public void setEndSelectNode(CommandEndSelect commandEndSelect) {
		this.commandEndSelect =commandEndSelect;
		// mark command end select as next for all cases
		for(var singleCase: cases) {
			singleCase.setEndSelect(commandEndSelect);
		}
	}

	public void registerCase(CommandCase node) {
		cases.add(node);		
	}

}

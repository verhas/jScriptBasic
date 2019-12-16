package com.scriptbasic.syntax.commands;

import com.scriptbasic.context.Context;
import com.scriptbasic.executors.commands.CommandSelect;
import com.scriptbasic.executors.commands.CommandWhile;
import com.scriptbasic.interfaces.AnalysisException;
import com.scriptbasic.interfaces.ScriptBasicKeyWords;
import com.scriptbasic.spi.Command;
import com.scriptbasic.spi.LeftValue;

public class CommandAnalyzerSelect
	extends AbstractCommandAnalyzer
{

	public CommandAnalyzerSelect(Context ctx)		
	{
		super(ctx);
	}

	@Override
	public Command analyze() throws AnalysisException {
		final var lexicalElement = ctx.lexicalAnalyzer.peek();
		// consume optional case statement
		if(lexicalElement.isSymbol(ScriptBasicKeyWords.KEYWORD_CASE)) 
			ctx.lexicalAnalyzer.get();
		// read expression till end of line
		final var expression = analyzeExpression();
		consumeEndOfLine();
		
		final var node = new CommandSelect();
		node.setExpression(expression);
		pushNode(node);
		
		return node;
	}

}

package com.scriptbasic.syntax.commands;

import com.scriptbasic.context.Context;
import com.scriptbasic.executors.commands.AbstractCommandSelectPart;
import com.scriptbasic.executors.commands.CommandCase;
import com.scriptbasic.executors.commands.CommandSelect;
import com.scriptbasic.interfaces.AnalysisException;
import com.scriptbasic.interfaces.BasicSyntaxException;
import com.scriptbasic.interfaces.ScriptBasicKeyWords;
import com.scriptbasic.spi.Command;

public class CommandAnalyzerCase
	extends AbstractCommandAnalyzer
{

	public CommandAnalyzerCase(Context ctx) {
		super(ctx);
	}

	@Override
	public Command analyze() throws AnalysisException {
		final var node = new CommandCase(); 
		// check if not else
		var elem = ctx.lexicalAnalyzer.peek();
		if(elem.isSymbol(ScriptBasicKeyWords.KEYWORD_ELSE)) {
			// no expression list -> do always
			ctx.lexicalAnalyzer.get();
		} else {
			analyzeCaseConditions(node);
		}		
		consumeEndOfLine();
		
        var lastSelectPart = ctx.nestedStructureHouseKeeper.pop(AbstractCommandSelectPart.class);
        CommandSelect commandSelect = null;                 
        if(lastSelectPart instanceof CommandCase) {
        	// pop real select
        	commandSelect = ctx.nestedStructureHouseKeeper.pop(CommandSelect.class);        	
        } else {
        	commandSelect = (CommandSelect)lastSelectPart;
        }
        // return select back to the stack
        pushNode(commandSelect);

        commandSelect.registerCase(node);
		
        pushNode(node);
		return node;
	}

	private void analyzeCaseConditions(CommandCase commandCase) throws AnalysisException {	
		while(true) {
			var elem = ctx.lexicalAnalyzer.peek();
			
			if(elem.isSymbol(ScriptBasicKeyWords.KEYWORD_IS)) {
				// skip is
				ctx.lexicalAnalyzer.get();
				// cosnume single expression
				var caseCondition = this.analyzeExpression();
				commandCase.addCaseEqualCondition(caseCondition);
			} else 
			{
				var caseCondition = this.analyzeExpression();
				elem = ctx.lexicalAnalyzer.peek();
				if(elem.isSymbol(ScriptBasicKeyWords.KEYWORD_TO)) {
					// skip to
					ctx.lexicalAnalyzer.get();
					// consume to expression
					var caseCondition2 = this.analyzeExpression();
					
					commandCase.addCaseFromToCondition(caseCondition, caseCondition2);
				} else {
					// has to be equal condition					
					commandCase.addCaseEqualCondition(caseCondition);
				}
			}
			
			// consume expression separator			
			elem = ctx.lexicalAnalyzer.peek();			
			if(elem.isSymbol(",")) {
				ctx.lexicalAnalyzer.get();
				continue;
			} else
			if(elem.isSymbol(ScriptBasicKeyWords.KEYWORD_IS)) {
				continue;
			} else
			if(elem.isLineTerminator()) {
				return;
			} else {
				// unexpected element
				throw new BasicSyntaxException("Unexpected token in case condition, value: "+elem.getLexeme());
			}
		}	
	}

}

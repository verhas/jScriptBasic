package com.scriptbasic.syntax.commands;

import com.scriptbasic.context.Context;
import com.scriptbasic.executors.commands.AbstractCommandSelectPart;
import com.scriptbasic.executors.commands.CommandCase;
import com.scriptbasic.executors.commands.CommandEndSelect;
import com.scriptbasic.executors.commands.CommandSelect;
import com.scriptbasic.interfaces.AnalysisException;
import com.scriptbasic.interfaces.BasicSyntaxException;
import com.scriptbasic.interfaces.ScriptBasicKeyWords;
import com.scriptbasic.spi.Command;

public class CommandAnalyzerEnd extends AbstractCommandAnalyzer {

    public CommandAnalyzerEnd(final Context ctx) {
        super(ctx);
    }

	@Override
	public Command analyze() throws AnalysisException {
        final var node = new CommandEndSelect();
        // select expected
        var elem = ctx.lexicalAnalyzer.get();
        if(!elem.isSymbol(ScriptBasicKeyWords.KEYWORD_SELECT)) {
            throw new BasicSyntaxException(
                    "Select has to be terminated with End Select statement");        	
        }
        consumeEndOfStatement();
        
        var lastSelectPart = ctx.nestedStructureHouseKeeper.pop(AbstractCommandSelectPart.class);

        
        CommandSelect commandSelect = null;                 
        if(lastSelectPart instanceof CommandCase) {
        	// pop real select
        	commandSelect = ctx.nestedStructureHouseKeeper.pop(CommandSelect.class);
        } else {
        	commandSelect = (CommandSelect)lastSelectPart;
        }
        commandSelect.setEndSelectNode(node);
        
        return node;
	}
}

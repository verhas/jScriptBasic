package com.scriptbasic.syntax.commandanalyzers;

import com.scriptbasic.exceptions.GenericSyntaxException;
import com.scriptbasic.executors.commands.CommandSub;
import com.scriptbasic.interfaces.AnalysisException;
import com.scriptbasic.interfaces.Command;
import com.scriptbasic.interfaces.LeftValueList;
import com.scriptbasic.interfaces.LexicalAnalyzer;
import com.scriptbasic.interfaces.LexicalElement;
import com.scriptbasic.utility.FactoryUtility;

public class CommandAnalyzerSub extends AbstractCommandAnalyzer {

    @Override
    public Command analyze() throws AnalysisException {
        CommandSub node = new CommandSub();
        LexicalAnalyzer lexicalAnalyzer = FactoryUtility
                .getLexicalAnalyzer(getFactory());

        LexicalElement lexicalElement = lexicalAnalyzer.get();
        if (lexicalElement.isIdentifier()) {
            String subName = lexicalElement.getLexeme();
            node.setSubName(subName);
        } else {
            throw new GenericSyntaxException(
                    "subroutine name has to follow the keyword " + getName());
        }
        assertKeyWord("(");
        LeftValueList arguments = analyzeLeftValueList();
        node.setArguments(arguments);
        assertKeyWord(")");
        pushNode(node);
        return node;
    }

    @Override
    protected String getName() {
        return "SUB";
    }

}

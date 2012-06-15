package com.scriptbasic.syntax.commandanalyzers;

import com.scriptbasic.exceptions.AnalysisException;
import com.scriptbasic.interfaces.CommandAnalyzer;
import com.scriptbasic.interfaces.Factory;
import com.scriptbasic.interfaces.LexicalElement;
import com.scriptbasic.syntax.AbstractAnalyzer;
import com.scriptbasic.utility.FactoryUtilities;
import com.scriptbasic.utility.SyntaxExceptionUtility;

public abstract class AbstractCommandAnalyzer extends AbstractAnalyzer
        implements CommandAnalyzer {
    private Factory factory;

    @Override
	public Factory getFactory() {
        return factory;
    }

    public void setFactory(Factory factory) {
        this.factory = factory;
    }

    abstract protected String getName();
    
    protected void assertThereAreNoSuperflouosCharactersOnTheLine() throws AnalysisException {
        LexicalElement le = FactoryUtilities.getLexicalAnalyzer(factory)
        .get();
        if (!le.isLineTerminator()) {
            SyntaxExceptionUtility.throwSyntaxException(
                    "There are extra characters following the expression after the '"
                            + getName() + "' keyword", le);
        }
    }
}

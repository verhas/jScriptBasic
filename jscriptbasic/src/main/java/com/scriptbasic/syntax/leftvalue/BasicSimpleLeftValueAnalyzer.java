package com.scriptbasic.syntax.leftvalue;

import com.scriptbasic.exceptions.GenericSyntaxException;
import com.scriptbasic.executors.leftvalues.BasicLeftValue;
import com.scriptbasic.interfaces.AnalysisException;
import com.scriptbasic.interfaces.Factory;
import com.scriptbasic.interfaces.LeftValue;
import com.scriptbasic.interfaces.LeftValueAnalyzer;
import com.scriptbasic.interfaces.LexicalAnalyzer;
import com.scriptbasic.interfaces.LexicalElement;
import com.scriptbasic.interfaces.SimpleLeftValueAnalyzer;
import com.scriptbasic.utility.FactoryUtility;

/**
 * Simple Left value is defined as
 * 
 * <pre>
 * SIMPLE LEFTVALUE ::= identifier
 * </pre>
 * 
 * @author Peter Verhas
 * @date July 15, 2012
 */
public class BasicSimpleLeftValueAnalyzer implements
        LeftValueAnalyzer, SimpleLeftValueAnalyzer {

    private Factory factory;

    public Factory getFactory() {
        return factory;
    }

    @Override
    public void setFactory(Factory factory) {
        this.factory = factory;
    }

    @Override
    public LeftValue analyze() throws AnalysisException {
        BasicLeftValue leftValue = null;
        LexicalAnalyzer lexicalAnalyzer = FactoryUtility
                .getLexicalAnalyzer(getFactory());
        LexicalElement lexicalElement = lexicalAnalyzer.peek();
        if (lexicalElement.isIdentifier()) {
            lexicalAnalyzer.get();
            leftValue = new BasicLeftValue();
            leftValue.setIdentifier(lexicalElement.getLexeme());
        } else {
            throw new GenericSyntaxException(
                    "left value should start with an identifier",
                    lexicalElement, null);
        }
        return leftValue;
    }
}

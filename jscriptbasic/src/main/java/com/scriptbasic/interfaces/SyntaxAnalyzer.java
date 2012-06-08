package com.scriptbasic.interfaces;

/**
 * Syntax analyzers should implement this interface.
 * 
 * @author Peter Verhas
 * 
 */
public interface SyntaxAnalyzer extends FactoryManaged {

    /**
     * Perform the syntax analysis.
     * 
     * @return
     * 
     * @throws SyntaxException
     */
    public Program analyze() throws SyntaxException, LexicalException;

}

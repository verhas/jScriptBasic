package com.scriptbasic.interfaces;

/**
 * Syntax analyzers should implement this interface.
 * 
 * @author Peter Verhas
 * 
 */
public interface SyntaxAnalyzer {
    /**
     * Set the lexical analyzer that provides the stream of the lexical
     * elements.
     * 
     * @param la
     *            the lexical analyzer.
     */
    public void setLexicalAnalyzer(LexicalAnalyzer la);

    /**
     * Get the lexical analyzer that the syntax analyzer is using.
     * 
     * @return
     */
    public LexicalAnalyzer getLexicalAnalyzer();

    /**
     * Perform the syntax analysis.
     * 
     * @throws SyntaxException
     */
    public void analyze() throws SyntaxException, LexicalException;

    /**
     * Get the program that was created during syntax analysis.
     * 
     * @return the executable object.
     */
    public Program getProgram();
}

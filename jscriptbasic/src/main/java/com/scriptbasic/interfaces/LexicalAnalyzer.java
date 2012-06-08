package com.scriptbasic.interfaces;

/**
 * A generic lexical analyzer that reads the characters from a reader and
 * returns the LexicalElements one after the other.
 * <p>
 * The actual implementation specifies what lexical elements that identifies and
 * returns.
 * 
 * @author Peter Verhas
 * 
 */
public interface LexicalAnalyzer extends FactoryManaged {
    /**
     * Set the reader from where the lexical analyzer has to read the input.
     * 
     * @param reader
     */
    public void set(Reader reader);

    /**
     * Get the next lexical element from the input stream. If there are no more
     * lexical elements then return {@code null}
     * 
     * @return
     */
    public LexicalElement get() throws LexicalException;

    /**
     * Peek at the next lexical element and do not remove it from the input
     * stream. Consecutive calls to {@code peek()} without calling {@see #get()}
     * will return the same lexical element. Calling {@see #get()} will return
     * the same lexical element as the last call to {@code peek()}.
     */
    public LexicalElement peek() throws LexicalException;

    /**
     * Register a lexical element analyzer. The lexical element analyzers are
     * consulted in the order they are registered to match a lexeme.
     * 
     * @param lea
     */
    public void registerElementAnalyzer(LexicalElementAnalyzer lea);
}

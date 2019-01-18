package com.scriptbasic.interfaces;


/**
 * A generic lexical analyzer that reads the characters from a reader and
 * returns the LexicalElements one after the other.
 * <p>
 * The actual implementation specifies what lexical elements that identifies and
 * returns.
 *
 * @author Peter Verhas
 */
public interface LexicalAnalyzer {

    /**
     * Get the next lexical element from the input stream. If there are no more
     * lexical elements then return {@code null}
     *
     * @return return the next lexical element
     * @throws AnalysisException in case there is an exception
     */
    LexicalElement get() throws AnalysisException;

    /**
     * Peek at the next lexical element and do not remove it from the input
     * stream. Consecutive calls to {@code peek()} without calling {@link #get()}
     * will return the same lexical element. Calling {@link #get()} will return
     * the same lexical element as the last call to {@code peek()}.
     * <p>
     * Just as {@link #get()} this method may also return {@code null} if there are no more
     * elements.
     *
     * @return the next lexical element
     * @throws AnalysisException in case of exception
     */
    LexicalElement peek() throws AnalysisException;

    /**
     * Register a lexical element analyzer. The lexical element analyzers are
     * consulted in the order they are registered to match a lexeme.
     *
     * @param lea parameter
     */
    void registerElementAnalyzer(LexicalElementAnalyzer lea);
}

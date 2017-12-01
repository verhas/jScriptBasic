package com.scriptbasic.interfaces;

/**
 * A lexical analyzer that supports syntax analyzers that analyze line oriented
 * language.
 *
 * @author Peter Verhas
 */
public interface LineOrientedLexicalAnalyzer extends LexicalAnalyzer {
    /**
     * Resets the state of the lexical analyzer so that the internal lexeme
     * pointer is reset to the first lexeme of the actual line and the next call
     * to {@link #get()} will return again the first lexeme of the line.
     */
    void resetLine();
}

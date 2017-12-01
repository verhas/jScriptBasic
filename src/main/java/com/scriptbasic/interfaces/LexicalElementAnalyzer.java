package com.scriptbasic.interfaces;

/**
 * A Lexical Element Analyzer analyzes the characters coming from a reader and
 * create a LexicalElement from it. If it can not create a lexical element then
 * it pushes back all characters read into the reader and the method
 * {@code read()} returns with {@code null}.
 * <p>
 * There are implementation of this interface to recognize string, symbol,
 * identifier, decimal number.
 *
 * @author Peter Verhas
 */
public interface LexicalElementAnalyzer {

    /**
     * Reads a lexeme and returns the created lexical element.
     * <p>
     * If the characters read from the reader show that the lexeme is not the
     * type that the class implementing this interface can handle then the
     * characters are pushed back to he reader and {@code null} is returned.
     * <p>
     * If the characters read from the reader show that the lexeme is the type
     * that the class implementing this interface can handle, but is erroneous,
     * then the method throws the exception.
     *
     * @return the created lexeme or {@code null}
     * @throws AnalysisException
     */
    LexicalElement read() throws AnalysisException;
}

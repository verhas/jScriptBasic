package com.scriptbasic.interfaces;

/**
 * Implementing class should recognize a string as a keyword or should tell that
 * the string is not a keyword. This helps the syntax analyzers that are used
 * for languages that have reserved words to identify the keywords on the
 * lexical analysis level.
 * 
 * @author Peter Verhas
 * 
 */
public interface KeywordRecognizer {
    /**
     * The method checks the argument if present in the reserved keyword list or
     * not.
     * 
     * @param identifier
     *            the string of the identifier that may be reserved word
     * 
     * @return {@code true} if and only if the identifier string is a reserved
     *         keyword
     */
    public boolean isRecognized(String identifier);
}

package com.scriptbasic.syntax;

import com.scriptbasic.errors.BasicInterpreterInternalError;
import com.scriptbasic.exceptions.GenericSyntaxException;
import com.scriptbasic.exceptions.LexicalException;
import com.scriptbasic.interfaces.AnalysisException;
import com.scriptbasic.interfaces.LexicalAnalyzer;
import com.scriptbasic.interfaces.LexicalElement;

/**
 * Utility class to help lexical analyzer usage.
 *
 * @author Peter Verhas
 * @date June 18, 2012
 *
 */
public final class LexFacade {

    /**
     * Private constructor to prevent instantiation of service class.
     */
    private LexFacade() {
        throw new BasicInterpreterInternalError(
                "Should not instantiate LexFacade utility class");
    }

    public static LexicalElement peek(final LexicalAnalyzer lexicalAnalyzer)
            throws AnalysisException {
        try {
            return lexicalAnalyzer.peek();
        } catch (final LexicalException e) {
            throw new GenericSyntaxException(e);
        }
    }

    public static LexicalElement get(final LexicalAnalyzer lexicalAnalyzer)
            throws AnalysisException {
        try {
            return lexicalAnalyzer.get();
        } catch (final LexicalException e) {
            throw new GenericSyntaxException(e);
        }
    }
}

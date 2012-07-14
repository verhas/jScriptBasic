/**
 * 
 */
package com.scriptbasic.utility;

import com.scriptbasic.exceptions.GenericSyntaxException;
import com.scriptbasic.exceptions.LexicalException;
import com.scriptbasic.interfaces.AnalysisException;
import com.scriptbasic.interfaces.Factory;
import com.scriptbasic.interfaces.LexicalAnalyzer;
import com.scriptbasic.interfaces.LexicalElement;

/**
 * @author Peter Verhas
 * @date Jun 28, 2012
 * 
 */
public final class LexUtility {
    private LexUtility() {
        UtilityUtility.throwExceptionToEnsureNobodyCallsIt();
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

    public static boolean isLexeme(Factory factory, String lexeme)
            throws AnalysisException {
        LexicalAnalyzer lexicalAnalyzer = FactoryUtility
                .getLexicalAnalyzer(factory);
        LexicalElement lexicalElement = lexicalAnalyzer.peek();
        if (lexicalElement != null && lexicalElement.isSymbol()
                && lexeme.equalsIgnoreCase(lexicalElement.getLexeme())) {
            lexicalAnalyzer.get();
            return true;
        }
        return false;
    }

    public static void checkLexeme(Factory factory, String lexeme,
            String exceptionText) throws AnalysisException {
        if (!isLexeme(factory, lexeme)) {
            throw new GenericSyntaxException(exceptionText);
        }
    }
}

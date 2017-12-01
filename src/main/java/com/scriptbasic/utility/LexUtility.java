package com.scriptbasic.utility;

import com.scriptbasic.interfaces.BasicSyntaxException;
import com.scriptbasic.exceptions.LexicalException;
import com.scriptbasic.interfaces.AnalysisException;
import com.scriptbasic.interfaces.LexicalAnalyzer;
import com.scriptbasic.interfaces.LexicalElement;

/**
 * @author Peter Verhas
 * date Jun 28, 2012
 * 
 */
public final class LexUtility {
    private LexUtility() {
        NoInstance.isPossible();
    }

    public static LexicalElement peek(final LexicalAnalyzer lexicalAnalyzer)
            throws AnalysisException {
        try {
            return lexicalAnalyzer.peek();
        } catch (final LexicalException e) {
            throw new BasicSyntaxException(e);
        }
    }

    public static LexicalElement get(final LexicalAnalyzer lexicalAnalyzer)
            throws AnalysisException {
        try {
            return lexicalAnalyzer.get();
        } catch (final LexicalException e) {
            throw new BasicSyntaxException(e);
        }
    }

    public static boolean isLexeme(LexicalAnalyzer analyzer, String lexeme)
            throws AnalysisException {
        LexicalElement lexicalElement = analyzer.peek();
        if (lexicalElement != null && lexicalElement.isSymbol()
                && lexeme.equalsIgnoreCase(lexicalElement.getLexeme())) {
            analyzer.get();
            return true;
        }
        return false;
    }

    public static void checkLexeme(LexicalAnalyzer analyzer, String lexeme,
            String exceptionText) throws AnalysisException {
        if (!isLexeme(analyzer, lexeme)) {
            throw new BasicSyntaxException(exceptionText);
        }
    }
}

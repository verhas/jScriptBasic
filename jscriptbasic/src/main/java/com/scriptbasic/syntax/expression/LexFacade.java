package com.scriptbasic.syntax.expression;

import com.scriptbasic.interfaces.LexicalAnalyzer;
import com.scriptbasic.interfaces.LexicalElement;
import com.scriptbasic.interfaces.LexicalException;
import com.scriptbasic.interfaces.SyntaxException;
import com.scriptbasic.syntax.GenericSyntaxException;

public class LexFacade {
    public static LexicalElement peek(final LexicalAnalyzer lexicalAnalyzer)
            throws SyntaxException {
        try {
            return lexicalAnalyzer.peek();
        } catch (final LexicalException e) {
            throw new GenericSyntaxException(e);
        }
    }

    public static LexicalElement get(final LexicalAnalyzer lexicalAnalyzer)
            throws SyntaxException {
        try {
            return lexicalAnalyzer.get();
        } catch (final LexicalException e) {
            throw new GenericSyntaxException(e);
        }
    }
}

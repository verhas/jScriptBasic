package com.scriptbasic.syntax.expression;

import com.scriptbasic.exceptions.GenericSyntaxException;
import com.scriptbasic.exceptions.LexicalException;
import com.scriptbasic.exceptions.SyntaxException;
import com.scriptbasic.interfaces.LexicalAnalyzer;
import com.scriptbasic.interfaces.LexicalElement;

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

package com.scriptbasic.lexer.elements;

import com.scriptbasic.interfaces.KeywordRecognizer;
import com.scriptbasic.interfaces.LexicalElement;
import com.scriptbasic.interfaces.LexicalException;
import com.scriptbasic.lexer.BasicLexialElementFactory;
import com.scriptbasic.lexer.BasicLexicalElement;

public class Identifier extends AbstractElementAnalyzer {

    private static final int IDENTIFIER_STRINGBUILDER_INITIAL_CAPACITY = 32;
    private KeywordRecognizer keywordRecognizer = null;

    public void setKeywordRecognizer(final KeywordRecognizer keywordRecognizer) {
        this.keywordRecognizer = keywordRecognizer;
    }

    @Override
    public LexicalElement read() throws LexicalException {
        Integer ch = getReader().get();
        if (Character.isUnicodeIdentifierStart(ch)) {
            final StringBuilder identifier = new StringBuilder(
                    IDENTIFIER_STRINGBUILDER_INITIAL_CAPACITY);
            while (ch != null && Character.isUnicodeIdentifierPart(ch)) {
                identifier.appendCodePoint(ch);
                ch = getReader().get();
            }
            getReader().pushBack(ch);
            final String s = identifier.toString();

            final BasicLexicalElement le = BasicLexialElementFactory
                    .create(getReader(),
                            keywordRecognizer.isRecognized(s) ? LexicalElement.TYPE_SYMBOL
                                    : LexicalElement.TYPE_IDENTIFIER);
            le.setLexeme(s);
            le.setStringValue(s);
            return le;
        } else {
            getReader().pushBack(ch);
            return null;
        }
    }
}

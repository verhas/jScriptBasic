package com.scriptbasic.lexer.elements;

import com.scriptbasic.exceptions.LexicalException;
import com.scriptbasic.interfaces.KeywordRecognizer;
import com.scriptbasic.interfaces.LexicalElement;
import com.scriptbasic.lexer.BasicLexialElementFactory;
import com.scriptbasic.readers.SourceReader;

public class Identifier extends AbstractElementAnalyzer {

    private static final int IDENTIFIER_STRINGBUILDER_INITIAL_CAPACITY = 32;
    private KeywordRecognizer keywordRecognizer = null;

    public Identifier(final SourceReader reader) {
        super(reader);
    }

    public void setKeywordRecognizer(final KeywordRecognizer keywordRecognizer) {
        this.keywordRecognizer = keywordRecognizer;
    }

    @Override
    public LexicalElement read() throws LexicalException {
        Integer ch = getReader().get();
        if (Character.isUnicodeIdentifierStart(ch)) {
            final var identifier = new StringBuilder(
                    IDENTIFIER_STRINGBUILDER_INITIAL_CAPACITY);
            while (ch != null && Character.isUnicodeIdentifierPart(ch)) {
                identifier.appendCodePoint(ch);
                ch = getReader().get();
            }
            getReader().unget(ch);
            final var s = identifier.toString();

            final var le = BasicLexialElementFactory
                    .create(getReader(),
                            this.keywordRecognizer.isRecognized(s) ? LexicalElement.TYPE_SYMBOL
                                    : LexicalElement.TYPE_IDENTIFIER);
            le.setLexeme(s);
            le.setStringValue(s);
            return le;
        } else {
            getReader().unget(ch);
            return null;
        }
    }
}

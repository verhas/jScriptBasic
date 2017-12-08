package com.scriptbasic.lexer.elements;

import com.scriptbasic.exceptions.LexicalException;
import com.scriptbasic.interfaces.LexicalElement;
import com.scriptbasic.interfaces.ScriptBasicKeyWords;
import com.scriptbasic.api.SourceReader;
import com.scriptbasic.lexer.BasicLexialElementFactory;
import com.scriptbasic.lexer.BasicLexicalElement;

public class MultiCharacter extends AbstractElementAnalyzer implements ScriptBasicKeyWords {


    public MultiCharacter(final SourceReader reader) {
        super(reader);
    }

    private void readAhead(final StringBuilder stringBuider,
                           final int totalCharsToReadAhead) {
        int charsToReadAhead = totalCharsToReadAhead;
        while (charsToReadAhead > 0) {
            final Integer ch = getReader().get();
            if (ch == null || Character.isWhitespace(ch)) {
                getReader().unget(ch);
                break;
            }
            stringBuider.appendCodePoint(ch);
            charsToReadAhead--;
        }
    }

    private void pushBack(final StringBuilder stringBuilder,
                          final int totalCharsToPushBack) {
        int charsToPushBack = totalCharsToPushBack;
        int pos = stringBuilder.length() - 1;
        while (charsToPushBack > 0) {
            getReader().unget((int) stringBuilder.charAt(pos));
            pos--;
            charsToPushBack--;
        }
    }

    @Override
    public LexicalElement read() throws LexicalException {
        BasicLexicalElement le = null;
        final StringBuilder sb = new StringBuilder(BASIC_OPERATOR_LEXEME_MAX_LENGTH);
        readAhead(sb, BASIC_OPERATOR_LEXEME_MAX_LENGTH);
        final String s = sb.toString();
        for (final String operator : BASIC_OPERATORS) {
            if (s.startsWith(operator)) {
                pushBack(sb, s.length() - operator.length());
                le = BasicLexialElementFactory.create(getReader(),
                        LexicalElement.TYPE_SYMBOL);
                le.setLexeme(operator);
                le.setStringValue(null);
                le.setType(LexicalElement.TYPE_SYMBOL);
                break;
            }
        }
        if (le == null) {
            pushBack(sb, sb.length());
        }
        return le;
    }
}

package com.scriptbasic.lexer.elements;

import java.io.IOException;

import com.scriptbasic.exceptions.LexicalException;
import com.scriptbasic.interfaces.LexicalElement;
import com.scriptbasic.lexer.BasicLexialElementFactory;
import com.scriptbasic.lexer.BasicLexicalElement;
import com.scriptbasic.utility.RegexpCommentFilter;
import com.scriptbasic.utility.TextFileResource;

public class MultiCharacter extends AbstractElementAnalyzer {
    private static String[] operators;
    private static int maxOperatorLength;

    static {
        try {
            operators = new TextFileResource(MultiCharacter.class,
                    "operators.txt").stripSpaces().stripEmptyLines()
                    .stripComments(new RegexpCommentFilter("^\\w.*"))
                    .getArray();
            maxOperatorLength = 0;
            for (final String s : operators) {
                if (s.length() > maxOperatorLength) {
                    maxOperatorLength = s.length();
                }
            }
        } catch (final IOException e) {
            operators = null;
        }
    }

    private void readAhead(final StringBuilder stringBuider,
            final int totalCharsToReadAhead) {
        int charsToReadAhead = totalCharsToReadAhead;
        while (charsToReadAhead > 0) {
            final Integer ch = getReader().get();
            if (ch == null || Character.isWhitespace(ch)) {
                getReader().pushBack(ch);
                break;
            }
            stringBuider.appendCodePoint(ch);
            charsToReadAhead--;
        }
    }

    private void pushBack(final StringBuilder stringBuilder,
            int totalCharsToPushBack) {
        int charsToPushBack = totalCharsToPushBack;
        int pos = stringBuilder.length() - 1;
        while (charsToPushBack > 0) {
            getReader().pushBack((int) stringBuilder.charAt(pos));
            pos--;
            charsToPushBack--;
        }
    }

    @Override
    public LexicalElement read() throws LexicalException {
        BasicLexicalElement le = null;
        final StringBuilder sb = new StringBuilder(maxOperatorLength);
        readAhead(sb, maxOperatorLength);
        final String s = sb.toString();
        for (final String operator : operators) {
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

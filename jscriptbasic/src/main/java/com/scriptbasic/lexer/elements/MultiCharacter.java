package com.scriptbasic.lexer.elements;

import java.io.IOException;

import com.scriptbasic.interfaces.LexicalElement;
import com.scriptbasic.interfaces.LexicalException;
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
            for (String s : operators) {
                if (s.length() > maxOperatorLength) {
                    maxOperatorLength = s.length();
                }
            }
        } catch (IOException e) {
            operators = null;
        }
    }

    private void readAhead(StringBuilder sb, int charsToReadAhead) {
        while (charsToReadAhead > 0) {
            Integer ch = getReader().get();
            if (ch == null || Character.isWhitespace(ch)) {
                getReader().pushBack(ch);
                break;
            }
            sb.appendCodePoint(ch);
            charsToReadAhead--;
        }
    }

    private void pushBack(StringBuilder sb, int charsToPushBack) {
        int pos = sb.length() - 1;
        while (charsToPushBack > 0) {
            getReader().pushBack((Integer) (int) sb.charAt(pos));
            pos--;
            charsToPushBack--;
        }
    }

    @Override
    public LexicalElement read() throws LexicalException {
        BasicLexicalElement le = null;
        StringBuilder sb = new StringBuilder(maxOperatorLength);
        readAhead(sb, maxOperatorLength);
        String s = sb.toString();
        for (String operator : operators) {
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

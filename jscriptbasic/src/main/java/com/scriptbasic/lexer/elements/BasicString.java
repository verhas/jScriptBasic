package com.scriptbasic.lexer.elements;

import com.scriptbasic.interfaces.LexicalElement;
import com.scriptbasic.interfaces.LexicalException;
import com.scriptbasic.lexer.BasicLexialElementFactory;
import com.scriptbasic.lexer.BasicLexicalElement;
import com.scriptbasic.lexer.UnterminatedStringException;

public class BasicString extends AbstractElementAnalyzer {

    @Override
    public LexicalElement read() throws LexicalException {
        BasicLexicalElement le = null;
        Integer ch = getReader().get();
        if (ch.equals((Integer) (int) '"')) {
            if (isTripleQuote()) {
                le = readMultiLineString();
            } else {
                le = readSingleLineString();
            }
        } else {
            getReader().pushBack(ch);
        }
        return le;
    }

    private boolean isTripleQuote() {
        boolean itIs = false;
        Integer second = getReader().get();
        Integer third = getReader().get();
        if (((Integer) (int) '"').equals(second)
                && ((Integer) (int) '"').equals(third)) {
            itIs = true;
        } else {
            getReader().pushBack(third);
            getReader().pushBack(second);
        }
        return itIs;
    }

    private static final int SINGLE_LINE_STRINGBUILDER_INITIAL_CAPACITY = 100;
    private static final int MULTI_LINE_STRINGBUILDER_INITIAL_CAPACITY = 1000;

    private boolean isStringTerminated(Integer ch, boolean multiLine)
            throws UnterminatedStringException {
        boolean terminated = false;
        if (ch == null) {
            throw new UnterminatedStringException(getReader());
        }
        if (ch.equals((Integer) (int) '"')) {
            if (multiLine) {
                terminated = isTripleQuote();
            } else {
                terminated = true;
            }
        }
        return terminated;
    }

    /**
     * Convert the character stored in {@code ch} to the character that this
     * character means in a string when there is a \ before it.
     * <p>
     * Very simple n to \n, t to \t and r to \r. Anything else remains itself.
     * Also " and ' and \.
     * <p>
     * Later versions should handle UNICODE escapes reading on from the
     * {@code reader}. //TODO
     * 
     * @param ch
     *            the character that was following the backslash.
     * 
     * @return the converted character.
     */
    private Integer convertEscapedChar(Integer ch) {
        if (ch != null) {
            switch (ch) {
            case 'n':
                ch = '\n';
                break;
            case 't':
                ch = '\t';
                break;
            case 'r':
                ch = '\r';
                break;
            }
        }
        return ch;
    }

    private void appendSeparator(StringBuilder sb, boolean multiLine) {
        sb.append(multiLine ? "\"\"\"" : "\"");
    }

    /**
     * Read a string and put it into a lexical element
     * 
     * @param stringBufferInitialSize
     * @param multiLine
     * @return
     */
    private BasicLexicalElement readString(int stringBufferInitialSize,
            boolean multiLine) throws UnterminatedStringException {

        StringBuilder string = new StringBuilder(stringBufferInitialSize);
        StringBuilder lexeme = new StringBuilder(stringBufferInitialSize);
        appendSeparator(lexeme, multiLine);

        BasicLexicalElement le = BasicLexialElementFactory.create(getReader(),
                LexicalElement.TYPE_STRING);

        Integer ch = getReader().get();
        while (!isStringTerminated(ch, multiLine)) {
            lexeme.appendCodePoint(ch);
            if (ch.equals((Integer) (int) '\\')) {
                ch = getReader().get();
                lexeme.appendCodePoint(ch);
                ch = convertEscapedChar(ch);
            }
            string.appendCodePoint(ch);
            ch = getReader().get();
        }
        appendSeparator(lexeme, multiLine);
        le.setLexeme(lexeme.toString());
        le.setStringValue(string.toString());
        le.setType(LexicalElement.TYPE_STRING);
        return le;
    }

    private BasicLexicalElement readSingleLineString()
            throws UnterminatedStringException {
        return readString(SINGLE_LINE_STRINGBUILDER_INITIAL_CAPACITY, false);
    }

    private BasicLexicalElement readMultiLineString()
            throws UnterminatedStringException {
        return readString(MULTI_LINE_STRINGBUILDER_INITIAL_CAPACITY, true);
    }
}

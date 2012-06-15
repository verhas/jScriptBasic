package com.scriptbasic.lexer.elements;

import com.scriptbasic.exceptions.LexicalException;
import com.scriptbasic.exceptions.UnterminatedStringException;
import com.scriptbasic.interfaces.LexicalElement;
import com.scriptbasic.lexer.BasicLexialElementFactory;
import com.scriptbasic.lexer.BasicLexicalElement;

public class BasicString extends AbstractElementAnalyzer {

    @Override
    public LexicalElement read() throws LexicalException {
        BasicLexicalElement le = null;
        final Integer ch = getReader().get();
        if (ch != null && ch.equals((int) '"')) {
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
        final Integer second = getReader().get();
        final Integer third = getReader().get();
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

    private boolean isStringTerminated(final Integer ch, final boolean multiLine)
            throws UnterminatedStringException {
        boolean terminated = false;
        if (ch == null) {
            throw new UnterminatedStringException(getReader());
        }
        if (ch.equals((int) '"')) {
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
    private static Integer convertEscapedChar(Integer ch) {
        if (ch != null) {
            switch (ch) {
            case 'n':
                ch = (int)'\n';
                break;
            case 't':
                ch = (int)'\t';
                break;
            case 'r':
                ch = (int)'\r';
                break;
            }
        }
        return ch;
    }

    private static void appendSeparator(final StringBuilder sb, final boolean multiLine) {
        sb.append(multiLine ? "\"\"\"" : "\"");
    }

    /**
     * Read a string and put it into a lexical element
     * 
     * @param stringBufferInitialSize
     * @param multiLine
     * @return
     */
    private BasicLexicalElement readString(final int stringBufferInitialSize,
            final boolean multiLine) throws UnterminatedStringException {

        final StringBuilder string = new StringBuilder(stringBufferInitialSize);
        final StringBuilder lexeme = new StringBuilder(stringBufferInitialSize);
        appendSeparator(lexeme, multiLine);

        final BasicLexicalElement le = BasicLexialElementFactory.create(
                getReader(), LexicalElement.TYPE_STRING);

        Integer ch = getReader().get();
        while (!isStringTerminated(ch, multiLine)) {
            lexeme.appendCodePoint(ch);
            if (ch.equals((int) '\\')) {
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

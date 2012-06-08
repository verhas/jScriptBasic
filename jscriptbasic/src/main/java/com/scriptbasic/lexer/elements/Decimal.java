package com.scriptbasic.lexer.elements;

import com.scriptbasic.interfaces.LexicalElement;
import com.scriptbasic.interfaces.LexicalException;
import com.scriptbasic.lexer.BasicLexialElementFactory;
import com.scriptbasic.lexer.BasicLexicalElement;

public class Decimal extends AbstractElementAnalyzer {

    private static final int DECIMAL_NUMBER_STRINGBUILDER_INITIAL_CAPACITY = 20;

    /**
     * Read a decimal number from the input. The next character following the
     * number is left in the input stream to be available for the next
     * {@code getReader().get()}.
     * <p>
     * A decimal number does NOT have {@code +} or {@code -} sign, because that
     * would cause trouble parsing expressions. It contains digits, optionally
     * decimal point and exponent part following 'e' or 'E'.
     * <p>
     * Some languages allow formats like {@code 1.} or {@code .12}. This parser
     * does not allow such unreadable format. If there is a decimal point then
     * there has to be a digit on each side.
     * <p>
     * The returned lexeme will be double if there is decimal point or exponent
     * part or both, even if the resulting value could be presented as long.
     * Thus {@code 1.12E2} is a double number and not a long containing
     * {@code 112}.
     * 
     * @param ch
     *            one character that was already read from the input
     * @return the lexical element containing the long or double value
     */
    @Override
    public LexicalElement read() throws LexicalException {

        Integer ch = getReader().get();
        getReader().pushBack(ch);
        if (ch != null && Character.isDigit(ch)) {
            final BasicLexicalElement le = BasicLexialElementFactory
                    .create(getReader());
            final StringBuilder digits = new StringBuilder(
                    DECIMAL_NUMBER_STRINGBUILDER_INITIAL_CAPACITY);
            processDigits(digits);
            ch = getReader().get();
            boolean floatFormat;
            if (((Integer) (int) '.').equals(ch)) {
                ch = getReader().get();
                if (ch != null && Character.isDigit(ch)) {
                    floatFormat = true;
                    digits.appendCodePoint('.');
                    getReader().pushBack(ch);
                    processDigits(digits);
                    processExponent(digits);
                } else {
                    floatFormat = false;
                    getReader().pushBack(ch);
                    getReader().pushBack((int) '.');
                }
            } else {
                getReader().pushBack(ch);
                floatFormat = processExponent(digits);
            }
            final String s = digits.toString();
            le.setLexeme(s);
            if (floatFormat) {
                le.setType(LexicalElement.TYPE_DOUBLE);
                le.setDoubleValue(Double.parseDouble(s));
            } else {
                le.setType(LexicalElement.TYPE_LONG);
                le.setLongValue(Long.parseLong(s));
            }
            return le;
        } else {
            return null;
        }
    }

    /**
     * Process the exponent part of a floating point number. The format of such
     * a part is {@code (E|e)[+|-]digits}
     * <p>
     * It leaves the first character not fitting the exponent part in the input
     * so that the next call to {@code getReader().get()} can fetch it.
     * 
     * @param exponentCharacters
     *            buffer to append the characters to
     * 
     * @return true if there was an exponent part
     */
    private boolean processExponent(final StringBuilder exponentCharacters) {
        boolean thereWasExponentPart = true;
        Integer ch = getReader().get();
        if (ch != null && (ch.equals((int) 'e') || ch.equals((int) 'E'))) {
            final Integer expChar = ch; // only to preserve the case: 'E' or 'e'
            ch = getReader().get();
            if (ch != null && (ch.equals((int) '-') || ch.equals((int) '+'))) {
                final Integer signChar = ch;
                ch = getReader().get();
                if (ch != null && Character.isDigit(ch)) {
                    exponentCharacters.appendCodePoint(expChar);
                    exponentCharacters.appendCodePoint(signChar);
                    getReader().pushBack(ch);
                    processDigits(exponentCharacters);
                } else {
                    getReader().pushBack(signChar);
                    getReader().pushBack(expChar);
                }
            } else {// if there is no + or -
                if (ch != null && Character.isDigit(ch)) {
                    exponentCharacters.appendCodePoint(expChar);
                    getReader().pushBack(ch);
                    processDigits(exponentCharacters);
                } else {
                    getReader().pushBack(expChar);
                }
            }
        } else {
            thereWasExponentPart = false;
            getReader().pushBack(ch);
        }
        return thereWasExponentPart;
    }

    /**
     * Read digits from the input until there is a non digit character. The read
     * digits are appended to the StringBuilder. The first non digit character
     * is left in the input to be fetched by the next call to
     * {@code getReader().get()}
     * 
     * @param digits
     *            to append the digits to
     * @param ch
     *            the first digit already fetched
     */
    private void processDigits(final StringBuilder digits) {
        Integer ch = getReader().get();
        while (ch != null && Character.isDigit(ch)) {
            digits.appendCodePoint(ch);
            ch = getReader().get();
        }
        getReader().pushBack(ch);
    }

}

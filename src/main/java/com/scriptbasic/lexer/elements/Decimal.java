package com.scriptbasic.lexer.elements;

import com.scriptbasic.exceptions.LexicalException;
import com.scriptbasic.interfaces.LexicalElement;
import com.scriptbasic.readers.SourceReader;
import com.scriptbasic.lexer.BasicLexialElementFactory;

public class Decimal extends AbstractElementAnalyzer {

    private static final int DECIMAL_NUMBER_STRINGBUILDER_INITIAL_CAPACITY = 20;

    public Decimal(final SourceReader reader) {
        super(reader);
    }

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
     * @return the lexical element containing the long or double value
     */
    @Override
    public LexicalElement read() {

        Integer ch = getReader().get();
        getReader().unget(ch);
        if (ch != null && Character.isDigit(ch)) {
            final var le = BasicLexialElementFactory
                    .create(getReader());
            final var digits = new StringBuilder(
                    DECIMAL_NUMBER_STRINGBUILDER_INITIAL_CAPACITY);
            processDigits(digits);
            ch = getReader().get();
            final boolean floatFormat;
            if (((Integer) (int) '.').equals(ch)) {
                floatFormat = processFraction(digits);
            } else {
                getReader().unget(ch);
                floatFormat = processExponent(digits);
            }
            final var s = digits.toString();
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
     * Process the fractional part of the number. The optional fractional part
     * starts with a '.' (dot) and is followed by decimal numbers.
     * <p>
     * If the '.' is not followed by digits then there is no fractional part of
     * the number and even if there is a '.' it is treated as stop character
     * that does not belong to the number and the '.' in this case is left on
     * the reader's character stream.
     *
     * @param fractionPart where the character are appended
     * @return {@code true} when there is a fractional part
     */
    private boolean processFraction(final StringBuilder fractionPart) {
        final boolean floatFormat;
        final var ch = getReader().get();
        if (ch != null && Character.isDigit(ch)) {
            floatFormat = true;
            fractionPart.appendCodePoint('.');
            getReader().unget(ch);
            processDigits(fractionPart);
            processExponent(fractionPart);
        } else {
            floatFormat = false;
            getReader().unget(ch);
            getReader().unget((int) '.');
        }
        return floatFormat;
    }

    /**
     * Process the {@code [eE](+|-)?\d+} part of the float number after the sign
     * character was recognized. The sign character '+' or '-' is passed in the
     * argument just as well as the 'e' or 'E' exponent character.
     *
     * @param exponentCharacters where the characters are put to form the lexeme.
     * @param signChar           the '+' or '-' character
     * @param expChar            the 'e' or 'E' character
     */
    private void processSignedExponenChars(
            final StringBuilder exponentCharacters, final Integer signChar,
            final Integer expChar) {

        final var ch = getReader().get();
        if (ch != null && Character.isDigit(ch)) {
            exponentCharacters.appendCodePoint(expChar);
            exponentCharacters.appendCodePoint(signChar);
            getReader().unget(ch);
            processDigits(exponentCharacters);
        } else {
            getReader().unget(signChar);
            getReader().unget(expChar);
        }
    }

    /**
     * Process the {@code [eE](+|-)?\d+} part of the float number. The starting
     * {@code [eE]} character is already read when this method is read and has
     * to be passed in the argument.
     * <p>
     * Process the exponent characters that follow the characters 'e' or 'E'. If
     * there is no '-' and/or '+' followed by a number then the 'e' or 'E'
     * (whichever is present on the input stream) is treated as stop character
     * and pushed back to the reader character stream.
     *
     * @param exponentCharacters where to put the characters
     * @param expChar            the 'e' or 'E' character. Used only to preserve the case of
     *                           the character for the lexeme and also to push it back to the
     *                           reader stream if the characters do not form an exponent part.
     */
    private void processExponenChars(final StringBuilder exponentCharacters,
                                     final Integer expChar) {
        final var ch = getReader().get();
        if (ch != null && (ch.equals((int) '-') || ch.equals((int) '+'))) {
            processSignedExponenChars(exponentCharacters, ch, expChar);
        } else {// if there is no + or -
            if (ch != null && Character.isDigit(ch)) {
                exponentCharacters.appendCodePoint(expChar);
                getReader().unget(ch);
                processDigits(exponentCharacters);
            } else {
                getReader().unget(expChar);
            }
        }
    }

    /**
     * Process the exponent part of a floating point number. The format of such
     * a part is {@code (E|e)[+|-]digits}
     * <p>
     * It leaves the first character not fitting the exponent part in the input
     * so that the next call to {@code getReader().get()} can fetch it.
     *
     * @param exponentCharacters buffer to append the characters to
     * @return true if there was an exponent part
     */
    private boolean processExponent(final StringBuilder exponentCharacters) {
        boolean thereWasExponentPart = true;
        final var ch = getReader().get();
        if (ch != null && (ch.equals((int) 'e') || ch.equals((int) 'E'))) {
            processExponenChars(exponentCharacters, ch);
        } else {
            thereWasExponentPart = false;
            getReader().unget(ch);
        }
        return thereWasExponentPart;
    }

    /**
     * Read digits from the input until there is a non digit character. The read
     * digits are appended to the StringBuilder. The first non digit character
     * is left in the input to be fetched by the next call to
     * {@code getReader().get()}
     *
     * @param digits to append the digits to
     */
    private void processDigits(final StringBuilder digits) {
        Integer ch = getReader().get();
        while (ch != null && Character.isDigit(ch)) {
            digits.appendCodePoint(ch);
            ch = getReader().get();
        }
        getReader().unget(ch);
    }

}

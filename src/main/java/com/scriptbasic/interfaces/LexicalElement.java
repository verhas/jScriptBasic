package com.scriptbasic.interfaces;

/**
 * A single lexical element that was created by the LexicalAnalyer
 *
 * @author Peter Verhas
 */
public interface LexicalElement extends SourceLocationBound {

    int TYPE_STRING = 0;
    int TYPE_DOUBLE = 1;
    int TYPE_LONG = 2;
    int TYPE_BOOLEAN = 3;
    int TYPE_IDENTIFIER = 4;
    int TYPE_SYMBOL = 5;

    /**
     * Get the original representation of the lexical element the way it was
     * specified in the source code.
     *
     * @return the lexical element as string
     */
    String getLexeme();

    /**
     * Get the type of the lexical element.
     *
     * @return
     */
    int getType();

    /**
     * Get the string value of the lexical element. This method should be called
     * only when the lexical element is a string literal. Otherwise the
     * implementation will throw IllegalArgumentException();
     *
     * @return
     */
    String stringValue();

    /**
     * Get the long value of the element. This method should only be called when
     * the type of the symbol is long. Otherwise the implementation will throw
     * IllegalArgumentException();
     *
     * @return
     * @throws IllegalArgumentException
     */
    Long longValue();

    Double doubleValue();

    Boolean booleanValue();

    Boolean isString();

    Boolean isDouble();

    Boolean isLong();

    Boolean isBoolean();

    Boolean isNumeric();

    Boolean isLiteralConstant();

    Boolean isIdentifier();

    Boolean isSymbol();

    /**
     * Return true if the lexical element is a symbol and the lexeme matches the
     * the actual symbol.
     *
     * @param lexeme to match by the lexical element. The parameter must not ne
     *               {@code null}.
     * @return true if the lexical element is a symbol and the lexeme matches
     * the parameter {@code lexeme}.
     */
    Boolean isSymbol(String lexeme);

    Boolean isLineTerminator();
}

package com.scriptbasic.lexer;

import com.scriptbasic.interfaces.LexicalElement;

public class MockLexicalElements extends BasicLexicalElement {

    MockLexicalElements(final String lexeme, final Integer type) {
        setType(type);
        setLexeme(lexeme);
    }

    MockLexicalElements(final String lexeme, final Double value) {
        setType(LexicalElement.TYPE_DOUBLE);
        setLexeme(lexeme);
        setDoubleValue(value);
    }

    MockLexicalElements(final String lexeme, final Long value) {
        setType(LexicalElement.TYPE_LONG);
        setLexeme(lexeme);
        setLongValue(value);
    }

    MockLexicalElements(final String lexeme, final String value) {
        setType(LexicalElement.TYPE_STRING);
        setLexeme(lexeme);
        setStringValue(value);
    }

    MockLexicalElements(final String lexeme, final Boolean value) {
        setType(LexicalElement.TYPE_BOOLEAN);
        setLexeme(lexeme);
        setBooleanValue(value);
    }
}

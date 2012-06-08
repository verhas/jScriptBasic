package com.scriptbasic.lexer;

import com.scriptbasic.interfaces.LexicalElement;

public class TestLE extends BasicLexicalElement {

    TestLE(final String lexeme, final Integer type) {
        setType(type);
        setLexeme(lexeme);
    }

    TestLE(final String lexeme, final Double value) {
        setType(LexicalElement.TYPE_DOUBLE);
        setLexeme(lexeme);
        setDoubleValue(value);
    }

    TestLE(final String lexeme, final Long value) {
        setType(LexicalElement.TYPE_LONG);
        setLexeme(lexeme);
        setLongValue(value);
    }

    TestLE(final String lexeme, final String value) {
        setType(LexicalElement.TYPE_STRING);
        setLexeme(lexeme);
        setStringValue(value);
    }

    TestLE(final String lexeme, final Boolean value) {
        setType(LexicalElement.TYPE_BOOLEAN);
        setLexeme(lexeme);
        setBooleanValue(value);
    }
}

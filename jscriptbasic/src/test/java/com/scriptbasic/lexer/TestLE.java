package com.scriptbasic.lexer;

import com.scriptbasic.interfaces.LexicalElement;

public class TestLE extends BasicLexicalElement {
    
    TestLE(String lexeme, Integer type) {
        setType(type);
        setLexeme(lexeme);
    }
    TestLE(String lexeme, Double value) {
        setType(LexicalElement.TYPE_DOUBLE);
        setLexeme(lexeme);
        setDoubleValue(value);
    }
    TestLE(String lexeme, Long value) {
        setType(LexicalElement.TYPE_LONG);
        setLexeme(lexeme);
        setLongValue(value);
    }
    TestLE(String lexeme, String value) {
        setType(LexicalElement.TYPE_STRING);
        setLexeme(lexeme);
        setStringValue(value);
    }
    TestLE(String lexeme, Boolean value) {
        setType(LexicalElement.TYPE_BOOLEAN);
        setLexeme(lexeme);
        setBooleanValue(value);
    }
}

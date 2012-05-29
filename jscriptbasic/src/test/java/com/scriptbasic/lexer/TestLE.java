package com.scriptbasic.lexer;

import com.scriptbasic.interfaces.LexicalElement;

public class TestLE extends BasicLexicalElement {
    TestLE(String lexeme, Integer type, Double doubleValue, Long longValue,
            String stringValue) {
        setType(type);
        setLexeme(lexeme);
        switch (type) {
        case LexicalElement.TYPE_DOUBLE:
            setDoubleValue(doubleValue);
            break;
        case LexicalElement.TYPE_LONG:
            setLongValue(longValue);
            break;
        case LexicalElement.TYPE_STRING:
            setStringValue(stringValue);
            break;
        case LexicalElement.TYPE_SYMBOL:
        case LexicalElement.TYPE_IDENTIFIER:
            break;
        }
    }

}

package com.scriptbasic.syntax;

import com.scriptbasic.interfaces.LexicalElement;

public class NullLexicalElement implements LexicalElement {
    @Override
    public String getLexeme() {
        return null;
    }

    @Override
    public int getType() {
        return 0;
    }

    @Override
    public String stringValue() {
        return null;
    }

    @Override
    public Long longValue() {
        return null;
    }

    @Override
    public Double doubleValue() {
        return null;
    }

    @Override
    public Boolean booleanValue() {
        return null;
    }

    @Override
    public Boolean isString() {
        return null;
    }

    @Override
    public Boolean isDouble() {
        return null;
    }

    @Override
    public Boolean isLong() {
        return null;
    }

    @Override
    public Boolean isBoolean() {
        return null;
    }

    @Override
    public Boolean isNumeric() {
        return null;
    }

    @Override
    public Boolean isLiteralConstant() {
        return null;
    }

    @Override
    public Boolean isIdentifier() {
        return null;
    }

    @Override
    public Boolean isSymbol() {
        return null;
    }

    @Override
    public Boolean isSymbol(String lexeme) {
        return null;
    }

    @Override
    public Boolean isLineTerminator() {
        return null;
    }

    @Override
    public String getFileName() {
        return null;
    }

    @Override
    public int getLineNumber() {
        return 0;
    }

    @Override
    public int getPosition() {
        return 0;
    }
}

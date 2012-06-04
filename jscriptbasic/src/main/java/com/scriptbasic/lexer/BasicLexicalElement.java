package com.scriptbasic.lexer;

import com.scriptbasic.utility.CharUtils;

public class BasicLexicalElement extends AbstractLexicalElement {
    private String fileName;
    private int lineNumber;
    private int position;
    private int type;
    private String lexeme;

    public void setLexeme(final String lexeme) {
        this.lexeme = lexeme;
    }

    public void setType(final int type) {
        this.type = type;
    }

    public void setLineNumber(final int lineNumber) {
        this.lineNumber = lineNumber;
    }

    public void setFileName(final String fileName) {
        this.fileName = fileName;
    }

    @Override
    public int position() {
        return this.position;
    }

    public void setPosition(final int position) {
        this.position = position;
    }

    @Override
    public String get() {
        return lexeme;
    }

    @Override
    public String fileName() {
        return this.fileName;
    }

    @Override
    public int lineNumber() {
        return this.lineNumber;
    }

    @Override
    public int type() {
        return this.type;
    }

    private String stringValue;
    private Long longValue;
    private Double doubleValue;
    private Boolean booleanValue;

    private void resetValues() {
        this.stringValue = null;
        this.doubleValue = null;
        this.longValue = null;
        this.booleanValue = null;
    }

    public void setStringValue(final String stringValue) {
        resetValues();
        this.stringValue = stringValue;
    }

    public void setLongValue(final long longValue) {
        resetValues();
        this.longValue = longValue;
    }

    public void setDoubleValue(final Double doubleValue) {
        resetValues();
        this.doubleValue = doubleValue;
    }

    public void setBooleanValue(final Boolean booleanValue) {
        resetValues();
        this.booleanValue = booleanValue;
    }

    @Override
    public String stringValue() throws IllegalArgumentException {
        return this.stringValue;
    }

    @Override
    public Long longValue() throws IllegalArgumentException {
        return this.longValue;
    }

    @Override
    public Double doubleValue() throws IllegalArgumentException {
        return this.doubleValue;
    }

    @Override
    public Boolean booleanValue() throws IllegalArgumentException {
        return this.booleanValue;
    }

    @Override
    public Boolean isBoolean() {
        return type() == TYPE_BOOLEAN;
    }

    @Override
    public Boolean isString() {
        return type() == TYPE_STRING;
    }

    @Override
    public Boolean isDouble() {
        return type() == TYPE_DOUBLE;
    }

    @Override
    public Boolean isLong() {
        return type() == TYPE_LONG;
    }

    @Override
    public Boolean isIdentifier() {
        return type() == TYPE_IDENTIFIER;
    }

    @Override
    public Boolean isSymbol() {
        return type() == TYPE_SYMBOL;
    }

    @Override
    public Boolean isLineTerminator() {
        return type() == TYPE_SYMBOL && get().length() == 1
                && CharUtils.isNewLine(get().codePointAt(0));
    }

}

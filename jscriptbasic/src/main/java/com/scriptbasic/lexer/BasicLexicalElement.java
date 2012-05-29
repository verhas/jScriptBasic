package com.scriptbasic.lexer;

import com.scriptbasic.interfaces.LexicalElement;
import com.scriptbasic.utility.CharUtils;

public class BasicLexicalElement implements LexicalElement {
    private String fileName;
    private int lineNumber;
    private int position;
    private int type;
    private String lexeme;

    public void setLexeme(String lexeme) {
        this.lexeme = lexeme;
    }

    public void setType(int type) {
        this.type = type;
    }

    public void setLineNumber(int lineNumber) {
        this.lineNumber = lineNumber;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public int position() {
        return this.position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public String get() {
        return lexeme;
    }

    public String fileName() {
        return this.fileName;
    }

    public int lineNumber() {
        return this.lineNumber;
    }

    public int type() {
        return this.type;
    }

    private String stringValue;
    private Long longValue;
    private Double doubleValue;

    public void setStringValue(String stringValue) {
        this.stringValue = stringValue;
        this.doubleValue = null;
        this.longValue = null;
    }

    public void setLongValue(long longValue) {
        this.doubleValue = null;
        this.stringValue = null;
        this.longValue = longValue;
    }

    public void setDoubleValue(Double doubleValue) {
        this.doubleValue = doubleValue;
        this.stringValue = null;
        this.longValue = null;
    }

    public String stringValue() throws IllegalArgumentException {
        return this.stringValue;
    }

    public Long longValue() throws IllegalArgumentException {
        return this.longValue;
    }

    public Double doubleValue() throws IllegalArgumentException {
        return this.doubleValue;
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

package com.scriptbasic.lexer;

import com.scriptbasic.interfaces.LexicalElement;

public class BasicLexicalElement implements LexicalElement {
	private String fileName;
	private int lineNumber;
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
	private long longValue;
	private Double doubleValue;
	
	public void setStringValue(String stringValue) {
		this.stringValue = stringValue;
	}

	public void setLongValue(long longValue) {
		this.longValue = longValue;
	}

	public void setDoubleValue(Double doubleValue) {
		this.doubleValue = doubleValue;
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

}

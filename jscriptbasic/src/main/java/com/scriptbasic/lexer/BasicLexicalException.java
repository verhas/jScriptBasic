package com.scriptbasic.lexer;

import com.scriptbasic.interfaces.LexicalException;
import com.scriptbasic.interfaces.Reader;

public class BasicLexicalException extends LexicalException {

	private static final long serialVersionUID = 8068731911556013119L;

	public BasicLexicalException() {
		super();
	}

	public BasicLexicalException(String arg0, Throwable arg1) {
		super(arg0, arg1);
	}

	public BasicLexicalException(String arg0) {
		super(arg0);
	}

	public BasicLexicalException(Throwable arg0) {
		super(arg0);
	}

	private String fileName;
	private int lineNumber;
	private int position;

	public void setPosition(Reader reader) {
		fileName = reader.fileName();
		lineNumber = reader.lineNumber();
		position = reader.position();
	}

	@Override
	public String fileName() {
		return fileName;
	}

	@Override
	public int lineNumber() {
		return lineNumber;
	}

	@Override
	public int position() {
		return position;
	}

}

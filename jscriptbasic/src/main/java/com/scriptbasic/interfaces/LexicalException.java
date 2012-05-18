package com.scriptbasic.interfaces;

public abstract class LexicalException extends Exception {

	private static final long serialVersionUID = -5235689307096026119L;

	public LexicalException() {
		super();
	}

	public LexicalException(String arg0, Throwable arg1) {
		super(arg0, arg1);
	}

	public LexicalException(String arg0) {
		super(arg0);
	}

	public LexicalException(Throwable arg0) {
		super(arg0);
	}

	abstract public String fileName();

	abstract public int lineNumber();

	abstract public int position();
}

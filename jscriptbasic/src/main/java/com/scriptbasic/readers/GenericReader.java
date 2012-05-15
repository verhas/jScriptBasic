package com.scriptbasic.readers;

import java.io.IOException;
import java.util.Deque;
import java.util.LinkedList;

import com.scriptbasic.interfaces.Reader;

public class GenericReader implements Reader {

	private java.io.Reader sourceReader;
	private Reader parent = null;
	private String sourceFileName = null;
	private int lineNumber = 0;
	private int position = 0;

	private Integer lastChar = null;

	public void set(java.io.Reader sourceReader) {
		this.sourceReader = sourceReader;
	}

	public void set(Reader parent) {
		this.parent = parent;
	}

	public void set(String sourceFileName) {
		this.sourceFileName = sourceFileName;
	}

	public String fileName() {
		return sourceFileName;
	}

	public int lineNumber() {
		return lineNumber;
	}

	public int position() {
		return position;
	}

	public Reader getParent() {
		return parent;
	}

	Deque<Integer> charsAhead = new LinkedList<Integer>();

	/**
	 * {@inheritDoc}
	 * 
	 * This implementation will not track the position properly when a new line
	 * character is pushed back
	 */
	public void pushBack(Integer ch) {
		charsAhead.addFirst(ch);
		position--;
	}

	public Integer get() {
		Integer nextChar;
		if (!charsAhead.isEmpty()) {
			return charsAhead.removeFirst();
		}

		try {
			nextChar = sourceReader.read();
		} catch (IOException e) {
			return null;
		}
		if (lastChar != null
				&& Character.getType(lastChar) == Character.LINE_SEPARATOR) {
			position = 0;
			lineNumber++;
		}
		lastChar = nextChar;
		return lastChar;
	}

}

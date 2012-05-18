package com.scriptbasic.readers;

import java.io.IOException;
import java.util.Deque;
import java.util.LinkedList;

import com.scriptbasic.interfaces.Reader;
import com.scriptbasic.interfaces.SourceProvider;
import com.scriptbasic.utility.CharacterCheck;

public class GenericReader implements Reader {

	private java.io.Reader sourceReader;
	private String sourceFileName = null;
	private int lineNumber = 0;
	private int position = 0;
	private SourceProvider sourceProvider = null;

	private Integer lastChar = null;

	public void set(java.io.Reader sourceReader) {
		this.sourceReader = sourceReader;
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

	Deque<Integer> charsAhead = new LinkedList<Integer>();

	/**
	 * {@inheritDoc}
	 * 
	 * This implementation will not track the position properly when a new line
	 * character is pushed back
	 */
	public void pushBack(Integer ch) {
		if (ch != null) {
			charsAhead.addFirst(ch);
			position--;
		}
	}

	public Integer get() {
		Integer nextChar;
		if (!charsAhead.isEmpty()) {
			position++;
			return charsAhead.removeFirst();
		}

		try {
			nextChar = sourceReader.read();
			if (nextChar == -1) {
				nextChar = null;
			}
		} catch (IOException e) {
			return null;
		}
		if (lastChar != null && CharacterCheck.isNewLine(lastChar)) {
			position = 0;
			lineNumber++;
		}
		position++;
		lastChar = nextChar;
		return lastChar;
	}

	public void setSourceProvider(SourceProvider sourceProvider) {
		this.sourceProvider = sourceProvider;
	}

	@Override
	public SourceProvider getSourceProvider() {
		return sourceProvider;
	}

}

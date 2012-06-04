package com.scriptbasic.readers;

import java.io.IOException;
import java.util.Deque;
import java.util.LinkedList;

import com.scriptbasic.interfaces.Reader;
import com.scriptbasic.interfaces.SourceProvider;
import com.scriptbasic.utility.CharUtils;

public class GenericReader implements Reader {

	private java.io.Reader sourceReader;
	private String sourceFileName = null;
	private int lineNumber = 0;
	private int position = 0;
	private SourceProvider sourceProvider = null;

	private Integer lastChar = null;

	public void set(final java.io.Reader sourceReader) {
		this.sourceReader = sourceReader;
	}

	@Override
    public void set(final String sourceFileName) {
		this.sourceFileName = sourceFileName;
	}

	@Override
    public String fileName() {
		return sourceFileName;
	}

	@Override
    public int lineNumber() {
		return lineNumber;
	}

	@Override
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
	@Override
    public void pushBack(final Integer ch) {
		if (ch != null) {
			charsAhead.addFirst(ch);
			position--;
		}else{
		    final Integer z = null;//TODO delete this, needed only to debug, have something here as a breakpoint
		}
	}

	@Override
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
		} catch (final IOException e) {
			return null;
		}
		if (lastChar != null && CharUtils.isNewLine(lastChar)) {
			position = 0;
			lineNumber++;
		}
		position++;
		lastChar = nextChar;
		return lastChar;
	}

	public void setSourceProvider(final SourceProvider sourceProvider) {
		this.sourceProvider = sourceProvider;
	}

	@Override
	public SourceProvider getSourceProvider() {
		return sourceProvider;
	}

}

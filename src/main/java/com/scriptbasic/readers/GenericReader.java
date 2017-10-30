package com.scriptbasic.readers;

import java.io.IOException;
import java.util.Deque;
import java.util.LinkedList;

import com.scriptbasic.interfaces.Factory;
import com.scriptbasic.interfaces.Reader;
import com.scriptbasic.interfaces.SourceProvider;
import com.scriptbasic.utility.CharUtils;

public class GenericReader implements Reader {
    @Override
    public void setFactory(Factory factory) {
    }

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
    public String getFileName() {
        return this.sourceFileName;
    }

    @Override
    public int getLineNumber() {
        return this.lineNumber;
    }

    @Override
    public int getPosition() {
        return this.position;
    }

    private Deque<Integer> charsAhead = new LinkedList<>();

    /**
     * {@inheritDoc}
     * 
     * This implementation will not track the position properly when a new line
     * character is pushed back
     */
    @Override
    public void pushBack(final Integer ch) {
        if (ch != null) {
            this.charsAhead.addFirst(ch);
            this.position--;
        }
    }

    @Override
    public Integer get() {
        Integer nextChar;
        if (!this.charsAhead.isEmpty()) {
            this.position++;
            return this.charsAhead.removeFirst();
        }

        try {
            do {
                nextChar = this.sourceReader.read();
            }while(isIgnored(nextChar));
            if (nextChar == -1) {
                nextChar = null;
            }
        } catch (final IOException e) {
            return null;
        }
        if (this.lastChar != null && CharUtils.isNewLine(this.lastChar)) {
            this.position = 0;
            this.lineNumber++;
        }
        this.position++;
        this.lastChar = nextChar;
        return this.lastChar;
    }

    private boolean isIgnored(Integer nextChar) {
        return nextChar == 13;
    }

    public void setSourceProvider(final SourceProvider sourceProvider) {
        this.sourceProvider = sourceProvider;
    }

    @Override
    public SourceProvider getSourceProvider() {
        return this.sourceProvider;
    }

}

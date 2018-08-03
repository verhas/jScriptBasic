package com.scriptbasic.readers;

import com.scriptbasic.utility.CharUtils;

import java.io.IOException;
import java.io.Reader;
import java.util.Deque;
import java.util.LinkedList;

public class GenericSourceReader implements SourceReader {
    private final Reader reader;
    private final SourceProvider sourceProvider;
    private final String sourceFileName;
    private int lineNumber = 0;
    private int position = 0;
    private Integer lastChar = null;
    private final Deque<Integer> charsAhead = new LinkedList<>();

    public GenericSourceReader(final Reader reader, final SourceProvider sourceProvider, final String sourceFileName) {
        this.sourceFileName = sourceFileName;
        this.reader = reader;
        this.sourceProvider = sourceProvider;
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

    /**
     * {@inheritDoc}
     * <p>
     * This implementation will not track the position properly when a new line
     * character is pushed back. Such a push back does not happen when using Basic lexical analysis.
     */
    @Override
    public void unget(final Integer ch) {
        if (ch != null) {
            this.charsAhead.addFirst(ch);
            this.position--;
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Integer get() {
        Integer nextChar;
        if (!this.charsAhead.isEmpty()) {
            this.position++;
            return this.charsAhead.removeFirst();
        }

        try {
            do {
                nextChar = this.reader.read();
            } while (isIgnored(nextChar));
            if (nextChar == -1) {
                nextChar = null;
            }
        } catch (final IOException e) {
            return null;
        }
        if (CharUtils.isNewLine(this.lastChar)) {
            this.position = 0;
            this.lineNumber++;
        }
        this.position++;
        this.lastChar = nextChar;
        return this.lastChar;
    }

    private boolean isIgnored(final Integer nextChar) {
        return nextChar == 13;
    }

    @Override
    public SourceProvider getSourceProvider() {
        return this.sourceProvider;
    }

}

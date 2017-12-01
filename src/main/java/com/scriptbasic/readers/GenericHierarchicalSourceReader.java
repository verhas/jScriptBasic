package com.scriptbasic.readers;

import com.scriptbasic.interfaces.HierarchicalSourceReader;
import com.scriptbasic.interfaces.SourceProvider;
import com.scriptbasic.interfaces.SourceReader;

import java.util.Stack;

public class GenericHierarchicalSourceReader implements HierarchicalSourceReader {

    public GenericHierarchicalSourceReader(SourceReader reader) {
        this.reader = reader;
    }

    private SourceReader reader;

    private final Stack<SourceReader> readerStack = new Stack<>();

    /**
     * Include a new reader into the chain and start to use that child reader so
     * long as long exhausts.
     * 
     * @param reader
     */
    @Override
    public void include(final SourceReader reader) {
        if (this.reader != null) {
            this.readerStack.push(this.reader);
        }
        this.reader = reader;
    }

    @Override
    public String getFileName() {
        return this.reader.getFileName();
    }

    @Override
    public int getLineNumber() {
        return this.reader.getLineNumber();
    }

    @Override
    public int getPosition() {
        return this.reader.getPosition();
    }

    @Override
    public void pushBack(final Integer ch) {
        reader.pushBack(ch);
    }

    /**
     * {@inheritDoc}
     * 
     * This version implements hierarchical reading. When a source finishes, it
     * returns to the parent reader and continues reading from there.
     */
    @Override
    public Integer get() {
        Integer ch = this.reader.get();
        while (ch == null && !this.readerStack.isEmpty()) {
            this.reader = this.readerStack.pop();
            ch = this.reader.get();
        }
        return ch;
    }

    @Override
    public SourceProvider getSourceProvider() {
        return this.reader.getSourceProvider();
    }
}

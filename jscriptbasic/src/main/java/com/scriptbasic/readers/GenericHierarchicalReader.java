package com.scriptbasic.readers;

import java.util.Stack;

import com.scriptbasic.interfaces.HierarchicalReader;
import com.scriptbasic.interfaces.Reader;
import com.scriptbasic.interfaces.SourceProvider;

public class GenericHierarchicalReader implements HierarchicalReader {
    private Reader reader;

    private final Stack<Reader> readerStack = new Stack<Reader>();

    /**
     * Include a new reader into the chain and start to use that child reader so
     * long as long exhausts.
     * 
     * @param reader
     */
    @Override
    public void include(final Reader reader) {
        if (this.reader != null) {
            readerStack.push(this.reader);
        }
        this.reader = reader;
    }

    @Override
    public void set(final String sourceFileName) {
        reader.set(sourceFileName);
    }

    @Override
    public String fileName() {
        return reader.fileName();
    }

    @Override
    public int lineNumber() {
        return reader.lineNumber();
    }

    @Override
    public int position() {
        return reader.position();
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
        Integer ch = reader.get();
        while (ch == null && !readerStack.isEmpty()) {
            reader = readerStack.pop();
            ch = reader.get();
        }
        return ch;
    }

    @Override
    public SourceProvider getSourceProvider() {
        return reader.getSourceProvider();
    }
}

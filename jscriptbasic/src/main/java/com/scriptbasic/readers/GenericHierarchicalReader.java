package com.scriptbasic.readers;

import java.util.Stack;

import com.scriptbasic.interfaces.HierarchicalReader;
import com.scriptbasic.interfaces.Reader;
import com.scriptbasic.interfaces.SourceProvider;

public class GenericHierarchicalReader implements HierarchicalReader {
    private Reader reader;

    private Stack<Reader> readerStack = new Stack<Reader>();

    /**
     * Include a new reader into the chain and start to use that child reader so
     * long as long exhausts.
     * 
     * @param reader
     */
    public void include(Reader reader) {
        if (this.reader != null) {
            readerStack.push(this.reader);
        }
        this.reader = reader;
    }

    public void set(String sourceFileName) {
        reader.set(sourceFileName);
    }

    public String fileName() {
        return reader.fileName();
    }

    public int lineNumber() {
        return reader.lineNumber();
    }

    public int position() {
        return reader.position();
    }

    public void pushBack(Integer ch) {
        reader.pushBack(ch);
    }

    /**
     * {@inheritDoc}
     * 
     * This version implements hierarchical reading. When a source finishes, it
     * returns to the parent reader and continues reading from there.
     */
    public Integer get() {
        Integer ch = reader.get();
        while (ch == null && !readerStack.isEmpty()) {
            reader = readerStack.pop();
            ch = reader.get();
        }
        return ch;
    }

    public SourceProvider getSourceProvider() {
        return reader.getSourceProvider();
    }
}

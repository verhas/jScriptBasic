package com.scriptbasic.lexer.elements;

import com.scriptbasic.exceptions.LexicalException;
import com.scriptbasic.interfaces.LexicalElement;
import com.scriptbasic.interfaces.LexicalElementAnalyzer;
import com.scriptbasic.api.SourceReader;

public abstract class AbstractElementAnalyzer implements LexicalElementAnalyzer {

    private final SourceReader reader;

    protected AbstractElementAnalyzer(final SourceReader reader) {
        this.reader = reader;
    }

    public SourceReader getReader() {
        return reader;
    }

    @Override
    public abstract LexicalElement read() throws LexicalException;

}

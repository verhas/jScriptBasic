package com.scriptbasic.lexer.elements;

import com.scriptbasic.exceptions.LexicalException;
import com.scriptbasic.interfaces.LexicalElement;
import com.scriptbasic.interfaces.LexicalElementAnalyzer;
import com.scriptbasic.interfaces.Reader;

public abstract class AbstractElementAnalyzer implements LexicalElementAnalyzer {

    private final Reader reader;

    protected AbstractElementAnalyzer(Reader reader) {
        this.reader = reader;
    }

    public Reader getReader() {
        return reader;
    }

    @Override
    public abstract LexicalElement read() throws LexicalException;

}

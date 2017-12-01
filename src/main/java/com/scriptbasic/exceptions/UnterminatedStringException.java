package com.scriptbasic.exceptions;

import com.scriptbasic.interfaces.SourceReader;

public class UnterminatedStringException extends BasicLexicalException {

    private static final long serialVersionUID = 2296291971733839357L;

    public UnterminatedStringException(final SourceReader reader) {
        super("Unterminated string");
        this.setPosition(reader);
    }

}

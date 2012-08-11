package com.scriptbasic.exceptions;
import com.scriptbasic.interfaces.Reader;
public class UnterminatedStringException extends BasicLexicalException {
    private static final long serialVersionUID = 2296291971733839357L;
    public UnterminatedStringException(final Reader reader) {
        super("Unterminated string");
        this.setPosition(reader);
    }
}
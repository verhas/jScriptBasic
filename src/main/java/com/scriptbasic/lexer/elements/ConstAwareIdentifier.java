package com.scriptbasic.lexer.elements;

import com.scriptbasic.exceptions.LexicalException;
import com.scriptbasic.interfaces.LexicalElement;
import com.scriptbasic.readers.SourceReader;
import com.scriptbasic.lexer.BasicLexicalElement;

public class ConstAwareIdentifier extends Identifier {

    public ConstAwareIdentifier(final SourceReader reader) {
        super(reader);
    }

    @Override
    public LexicalElement read() throws LexicalException {
        final var lexicalElement = (BasicLexicalElement) super
                .read();
        if (lexicalElement != null) {
            Boolean value = null;
            if ("true".equalsIgnoreCase(lexicalElement.getLexeme())) {
                value = true;
            } else if ("false".equalsIgnoreCase(lexicalElement.getLexeme())) {
                value = false;
            }
            if (value != null) {
                lexicalElement.setType(LexicalElement.TYPE_BOOLEAN);
                lexicalElement.setBooleanValue(value);
            }
        }
        return lexicalElement;
    }
}

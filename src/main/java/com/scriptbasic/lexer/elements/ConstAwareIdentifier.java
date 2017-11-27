package com.scriptbasic.lexer.elements;

import com.scriptbasic.exceptions.LexicalException;
import com.scriptbasic.interfaces.LexicalElement;
import com.scriptbasic.interfaces.Reader;
import com.scriptbasic.lexer.BasicLexicalElement;

public class ConstAwareIdentifier extends Identifier {

    public ConstAwareIdentifier(Reader reader) {
        super(reader);
    }

    @Override
    public LexicalElement read() throws LexicalException {
        final BasicLexicalElement lexicalElement = (BasicLexicalElement) super
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

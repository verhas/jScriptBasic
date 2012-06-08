package com.scriptbasic.lexer.elements;

import com.scriptbasic.interfaces.LexicalElement;
import com.scriptbasic.interfaces.LexicalException;
import com.scriptbasic.lexer.BasicLexicalElement;

public class ConstAwareIdentifier extends Identifier {

    @Override
    public LexicalElement read() throws LexicalException {
        final BasicLexicalElement lexicalElement = (BasicLexicalElement) super
                .read();
        if (lexicalElement != null) {
            Boolean value = null;
            if ("true".equalsIgnoreCase(lexicalElement.get())) {
                value = true;
            } else if ("false".equalsIgnoreCase(lexicalElement.get())) {
                value = false;
            }
            if (value != null) {
                lexicalElement.setType(LexicalElement.TYPE_BOOLEAN);
                lexicalElement.setBooleanValue(value);
            }
            return lexicalElement;
        } else {
            return null;
        }
    }
}

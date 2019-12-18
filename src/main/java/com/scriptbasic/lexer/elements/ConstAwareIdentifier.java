package com.scriptbasic.lexer.elements;

import com.scriptbasic.exceptions.LexicalException;
import com.scriptbasic.interfaces.LexicalElement;
import com.scriptbasic.interfaces.ScriptBasicKeyWords;
import com.scriptbasic.lexer.BasicLexicalElement;
import com.scriptbasic.readers.SourceReader;

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
            if (ScriptBasicKeyWords.KEYWORD_TRUE.equalsIgnoreCase(lexicalElement.getLexeme())) {
                value = true;
            } else if (ScriptBasicKeyWords.KEYWORD_FALSE.equalsIgnoreCase(lexicalElement.getLexeme())) {
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

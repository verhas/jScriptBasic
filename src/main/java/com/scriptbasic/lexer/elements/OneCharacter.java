package com.scriptbasic.lexer.elements;

import com.scriptbasic.exceptions.LexicalException;
import com.scriptbasic.interfaces.LexicalElement;
import com.scriptbasic.api.SourceReader;
import com.scriptbasic.lexer.BasicLexialElementFactory;
import com.scriptbasic.lexer.BasicLexicalElement;
import com.scriptbasic.utility.CharUtils;

public class OneCharacter extends AbstractElementAnalyzer {

    public OneCharacter(final SourceReader reader) {
        super(reader);
    }

    @Override
    public LexicalElement read() throws LexicalException {
        final Integer ch = getReader().get();
        if (ch != null) {
            final BasicLexicalElement le = BasicLexialElementFactory.create(
                    getReader(), LexicalElement.TYPE_SYMBOL);
            le.setLexeme(CharUtils.convert(ch));
            le.setType(LexicalElement.TYPE_SYMBOL);
            return le;
        } else {
            return null;
        }
    }
}

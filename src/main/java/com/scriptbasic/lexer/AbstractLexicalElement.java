package com.scriptbasic.lexer;

import com.scriptbasic.interfaces.LexicalElement;

public abstract class AbstractLexicalElement implements LexicalElement {

    @Override
    public final Boolean isNumeric() {
        return isLong() || isDouble();
    }

    @Override
    public final Boolean isLiteralConstant() {
        return isNumeric() || isString() || isBoolean();
    }

}

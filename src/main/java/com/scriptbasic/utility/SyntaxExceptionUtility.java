package com.scriptbasic.utility;

import com.scriptbasic.exceptions.SyntaxException;
import com.scriptbasic.interfaces.BasicSyntaxException;
import com.scriptbasic.interfaces.LexicalElement;

public final class SyntaxExceptionUtility {

    private SyntaxExceptionUtility() {
        NoInstance.isPossible();
    }

    public static void throwSyntaxException(final String s, final LexicalElement le)
            throws SyntaxException {
        final var se = new BasicSyntaxException(s);
        if (le != null) {
            se.setLocation(le);
        }
        throw se;
    }

}

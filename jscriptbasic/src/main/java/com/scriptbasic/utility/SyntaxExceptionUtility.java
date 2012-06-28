package com.scriptbasic.utility;

import com.scriptbasic.exceptions.GenericSyntaxException;
import com.scriptbasic.exceptions.SyntaxException;
import com.scriptbasic.interfaces.LexicalElement;

public class SyntaxExceptionUtility {

    private SyntaxExceptionUtility() {
        UtilityUtility.assertUtilityClass();
    }

    public static void throwSyntaxException(String s, LexicalElement le)
            throws SyntaxException {
        SyntaxException se = new GenericSyntaxException(s);
        if (le != null) {
            se.setLocation(le);
        }
        throw se;
    }

}

package com.scriptbasic.utility;

import com.scriptbasic.interfaces.LexicalElement;
import com.scriptbasic.interfaces.SyntaxException;
import com.scriptbasic.syntax.GenericSyntaxException;

public class SyntaxExceptionUtility {

    public static void throwSyntaxException(String s, LexicalElement le) throws SyntaxException{
        SyntaxException se = new GenericSyntaxException(s);
        se.setLocation(le);
        throw se;
    }
    
}

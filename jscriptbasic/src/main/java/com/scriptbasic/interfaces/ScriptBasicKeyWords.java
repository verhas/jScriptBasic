/**
 * 
 */
package com.scriptbasic.interfaces;

/**
 * @author Peter Verhas
 * @date June 26, 2012
 * 
 */
public interface ScriptBasicKeyWords {
    String[] keywordArray = new String[] { "for", "end", "next", "let", "if",
            "endif", "then", "while", "wend", "else", "elseif", "repeat",
            "until", "not", "false", "true", "and", "or", "div", "use", "from",
            "as", "function" , "endfunction", "to", "step", "method", "is" };
    String[] operators = new String[] { "<=", ">=", "<>" };
    int maxOperatorLength = 2;
}

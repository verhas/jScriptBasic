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
    String[] BASIC_KEYWORDS = new String[] { "for", "end", "next", "let", "if",
            "endif", "then", "while", "wend", "else", "elseif", "repeat",
            "until", "not", "false", "true", "and", "or", "div", "use", "from",
            "as", "function", "endfunction", "to", "step", "method", "is",
            "rem" };
    String[] BASIC_OPERATORS = new String[] { "<=", ">=", "<>" };
    int BASIC_OPERATOR_LEXEME_MAX_LENGTH = 2;
}

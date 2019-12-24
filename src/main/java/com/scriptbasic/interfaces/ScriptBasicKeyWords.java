package com.scriptbasic.interfaces;

/**
 * @author Peter Verhas
 * date June 26, 2012
 */
public interface ScriptBasicKeyWords {
    String KEYWORD_AND = "and";
    String KEYWORD_AS = "as";
    String KEYWORD_CALL = "call";
    String KEYWORD_CASE = "case";
    String KEYWORD_DIV = "div";
    String KEYWORD_ELSE = "else";
    String KEYWORD_ELSEIF = "elseif";
    String KEYWORD_END = "end";
    String KEYWORD_ENDIF = "endif";
    String KEYWORD_ENDSUB = "endsub";
    String KEYWORD_FALSE = "false";
    String KEYWORD_FOR = "for";
    String KEYWORD_FROM = "from";
    String KEYWORD_GLOBAL = "global";
    String KEYWORD_IF = "if";
    String KEYWORD_IS = "is";
    String KEYWORD_LET = "let";
    String KEYWORD_LOCAL = "local";
    String KEYWORD_METHOD = "method";
    String KEYWORD_NEXT = "next";
    String KEYWORD_NOT = "not";
    String KEYWORD_OR = "or"; 
    String KEYWORD_PRINT = "print";
    String KEYWORD_REM = "rem";
    String KEYWORD_REPEAT = "repeat";
    String KEYWORD_RETURN = "return";
    String KEYWORD_SELECT = "select";
    String KEYWORD_SENTENCE = "sentence";
    String KEYWORD_STEP = "step";
    String KEYWORD_SUB = "sub";
    String KEYWORD_THEN = "then";
    String KEYWORD_TO = "to";
    String KEYWORD_TRUE = "true";
    String KEYWORD_UNTIL = "until";
    String KEYWORD_USE = "use";
    String KEYWORD_WEND = "wend";
    String KEYWORD_WHILE = "while";
    
    String OPERATOR_PLUS = "+";
    String OPERATOR_MINUS = "-";
    String OPERATOR_EQUALS = "=";
    String OPERATOR_LESS = "<";
    String OPERATOR_GREATER = ">";
    String OPERATOR_GREATER_OR_EQUAL = ">=";
    String OPERATOR_LESS_OR_EQUAL = "<=";
    String OPERATOR_NOT_EQUALS = "<>";
    String OPERATOR_MULTIPLY = "*";
    String OPERATOR_DIVIDE = "/";
    String OPERATOR_MODULO = "%";
    String OPERATOR_POWER = "^";
    String OPERATOR_AMPERSAND = "&";

    /** 
     * Collection of all keywords, used by BasicKeywordRecognizer
     * 
     * <p>
     * Note: true, false (KEYWORD_TRUE, KEYWORD_FALSE) are constants thus not
     * included in the collection of keywords
     * </p>
     */
    String[] BASIC_KEYWORDS = new String[]{KEYWORD_FOR, KEYWORD_END, KEYWORD_NEXT, 
            KEYWORD_LET, KEYWORD_GLOBAL, KEYWORD_LOCAL,
            KEYWORD_IF, KEYWORD_ENDIF, KEYWORD_THEN, KEYWORD_ELSE, KEYWORD_ELSEIF, 
            KEYWORD_WHILE, KEYWORD_WEND, 
            KEYWORD_REPEAT, KEYWORD_UNTIL, 
            KEYWORD_NOT, KEYWORD_AND, KEYWORD_OR, 
            KEYWORD_DIV, 
            KEYWORD_USE, KEYWORD_FROM,
            KEYWORD_AS, KEYWORD_TO, KEYWORD_STEP, KEYWORD_METHOD, KEYWORD_IS, 
            KEYWORD_REM, 
            KEYWORD_SUB, KEYWORD_ENDSUB,
            KEYWORD_RETURN, KEYWORD_PRINT, 
            KEYWORD_CALL,
            KEYWORD_CASE, KEYWORD_SELECT,
            KEYWORD_SENTENCE};
    String[] BASIC_OPERATORS = new String[]{OPERATOR_LESS_OR_EQUAL, OPERATOR_GREATER_OR_EQUAL, OPERATOR_NOT_EQUALS };
    int BASIC_OPERATOR_LEXEME_MAX_LENGTH = 2;
}

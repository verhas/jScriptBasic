package com.scriptbasic.interfaces;

/**
 * @author Peter Verhas
 * date June 26, 2012
 */
public interface ScriptBasicKeyWords {
	public static final String KEYWORD_CASE = "case";
	public static final String KEYWORD_END = "end";
	public static final String KEYWORD_ELSE = "else";
	public static final String KEYWORD_IS = "is";
	public static final String KEYWORD_SELECT = "select";
	public static final String KEYWORD_TO = "to";
	
    String[] BASIC_KEYWORDS = new String[]{"for", KEYWORD_END, "next", "let", "if",
            "endif", "then", "while", "wend", KEYWORD_ELSE, "elseif", "repeat",
            "until", "not", "false", "true", "and", "or", "div", "use", "from",
            "as", KEYWORD_TO, "step", "method", KEYWORD_IS, "rem", "sub", "endsub",
            "return", "print", "global", "local", "call", KEYWORD_SELECT,
            KEYWORD_CASE, "sentence"};
    String[] BASIC_OPERATORS = new String[]{"<=", ">=", "<>"};
    int BASIC_OPERATOR_LEXEME_MAX_LENGTH = 2;
}

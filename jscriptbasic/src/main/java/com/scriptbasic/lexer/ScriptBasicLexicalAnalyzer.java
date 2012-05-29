package com.scriptbasic.lexer;

import com.scriptbasic.interfaces.KeywordRecognizer;
import com.scriptbasic.lexer.elements.BasicString;
import com.scriptbasic.lexer.elements.Decimal;
import com.scriptbasic.lexer.elements.Identifier;
import com.scriptbasic.lexer.elements.MultiCharacter;
import com.scriptbasic.lexer.elements.OneCharacter;

public class ScriptBasicLexicalAnalyzer extends BasicLexicalAnalyzer {
    public ScriptBasicLexicalAnalyzer() {
        Identifier identifier = new Identifier();
        KeywordRecognizer keywordRecognizer = new BasicKeywordRecognizer();
        identifier.setKeywordRecognizer(keywordRecognizer);
        registerElementAnalyzer(identifier);
        registerElementAnalyzer(new Decimal());
        registerElementAnalyzer(new BasicString());
        registerElementAnalyzer(new MultiCharacter());
        registerElementAnalyzer(new OneCharacter());
    }
}

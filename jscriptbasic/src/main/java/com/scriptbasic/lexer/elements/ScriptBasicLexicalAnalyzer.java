package com.scriptbasic.lexer.elements;

import com.scriptbasic.interfaces.KeywordRecognizer;
import com.scriptbasic.lexer.BasicKeywordRecognizer;
import com.scriptbasic.lexer.BasicLexicalAnalyzer;

public final class ScriptBasicLexicalAnalyzer extends BasicLexicalAnalyzer {
    private ScriptBasicLexicalAnalyzer() {
        final Identifier identifier = new ConstAwareIdentifier();
        final KeywordRecognizer keywordRecognizer = new BasicKeywordRecognizer();
        identifier.setKeywordRecognizer(keywordRecognizer);
        registerElementAnalyzer(identifier);
        registerElementAnalyzer(new Decimal());
        registerElementAnalyzer(new BasicString());
        registerElementAnalyzer(new MultiCharacter());
        registerElementAnalyzer(new OneCharacter());
    }
}

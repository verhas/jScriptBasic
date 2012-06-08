package com.scriptbasic.lexer;

import com.scriptbasic.interfaces.KeywordRecognizer;
import com.scriptbasic.lexer.elements.BasicString;
import com.scriptbasic.lexer.elements.ConstAwareIdentifier;
import com.scriptbasic.lexer.elements.Decimal;
import com.scriptbasic.lexer.elements.Identifier;
import com.scriptbasic.lexer.elements.MultiCharacter;
import com.scriptbasic.lexer.elements.OneCharacter;

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

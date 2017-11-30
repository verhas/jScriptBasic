package com.scriptbasic.lexer.elements;

import com.scriptbasic.interfaces.KeywordRecognizer;
import com.scriptbasic.interfaces.SourceReader;
import com.scriptbasic.lexer.BasicKeywordRecognizer;
import com.scriptbasic.lexer.BasicLexicalAnalyzer;

public final class ScriptBasicLexicalAnalyzer extends BasicLexicalAnalyzer {
    public ScriptBasicLexicalAnalyzer(SourceReader reader) {
        super(reader);
        final Identifier identifier = new ConstAwareIdentifier(reader);
        final KeywordRecognizer keywordRecognizer = new BasicKeywordRecognizer();
        identifier.setKeywordRecognizer(keywordRecognizer);
        registerElementAnalyzer(identifier);
        registerElementAnalyzer(new Decimal(reader));
        registerElementAnalyzer(new BasicString(reader));
        registerElementAnalyzer(new MultiCharacter(reader));
        registerElementAnalyzer(new OneCharacter(reader));
    }
}

package com.scriptbasic.lexer.elements;

import com.scriptbasic.lexer.BasicKeywordRecognizer;
import com.scriptbasic.lexer.BasicLexicalAnalyzer;
import com.scriptbasic.readers.SourceReader;

public final class ScriptBasicLexicalAnalyzer extends BasicLexicalAnalyzer {
    public ScriptBasicLexicalAnalyzer(final SourceReader reader) {
        super(reader);
        final var identifier = new ConstAwareIdentifier(reader);
        final var keywordRecognizer = new BasicKeywordRecognizer();
        identifier.setKeywordRecognizer(keywordRecognizer);
        registerElementAnalyzer(identifier);
        registerElementAnalyzer(new Decimal(reader));
        registerElementAnalyzer(new BasicString(reader));
        registerElementAnalyzer(new MultiCharacter(reader));
        registerElementAnalyzer(new OneCharacter(reader));
    }
}

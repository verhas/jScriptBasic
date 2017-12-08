package com.scriptbasic.lexer;

import com.scriptbasic.interfaces.AnalysisException;
import com.scriptbasic.interfaces.LexicalAnalyzer;
import com.scriptbasic.interfaces.LexicalElement;
import com.scriptbasic.readers.SourceReader;
import com.scriptbasic.lexer.elements.ScriptBasicLexicalAnalyzer;
import com.scriptbasic.log.Logger;
import com.scriptbasic.log.LoggerFactory;
import com.scriptbasic.readers.GenericSourceReader;
import com.scriptbasic.sourceproviders.StringSourceProvider;
import org.junit.Assert;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;

import static org.junit.Assert.assertTrue;

public class LexTestHelper {
    static final StringBuilder apo = new StringBuilder("\"");
    static final StringBuilder apoE = new StringBuilder("\\\"");
    static final StringBuilder newLine = new StringBuilder("\n");
    static final StringBuilder newLineE = new StringBuilder("\\n");
    static final StringBuilder tab = new StringBuilder("\t");
    static final StringBuilder tabE = new StringBuilder("\\t");
    static final StringBuilder cr = new StringBuilder("\r");
    static final StringBuilder crE = new StringBuilder("\\r");
    static final StringBuilder bs = new StringBuilder("\\");
    static final StringBuilder bsE = new StringBuilder("\\\\");
    private static final Logger LOG = LoggerFactory.getLogger();

    static MockLexicalElements ID(final String name) {
        return new MockLexicalElements(name, LexicalElement.TYPE_IDENTIFIER);
    }

    static MockLexicalElements LONG(final String s) {
        final Long l = Long.parseLong(s);
        return new MockLexicalElements(s, l);
    }

    static MockLexicalElements DOUBLE(final String s) {
        final Double d = Double.parseDouble(s);
        return new MockLexicalElements(s, d);
    }

    static MockLexicalElements BOOL(final Boolean b) {
        return new MockLexicalElements(b.toString(), b);
    }

    static MockLexicalElements BOOL(final String b) {
        return new MockLexicalElements(b, Boolean.parseBoolean(b));
    }

    static String string2Lexeme(String s) {
        s = s.replace(bs, bsE);
        s = s.replace(newLine, newLineE);
        s = s.replace(tab, tabE);
        s = s.replace(cr, crE);
        return s;
    }

    static String sstring2Lexeme(String s) {
        s = string2Lexeme(s);
        s = s.replace(apo, apoE);
        return s;
    }

    static MockLexicalElements SSTRING(final String s) {
        final String lexeme = sstring2Lexeme(s);
        return SSTRING(s, lexeme);
    }

    static MockLexicalElements SSTRING(final String s, final String lexeme) {
        return new MockLexicalElements("\"" + lexeme + "\"", s);
    }

    static MockLexicalElements MSTRING(final String s) {
        final String lexeme = string2Lexeme(s);
        return MSTRING(s, lexeme);
    }

    static MockLexicalElements MSTRING(final String s, final String lexeme) {
        return new MockLexicalElements("\"\"\"" + lexeme + "\"\"\"", s);
    }

    static MockLexicalElements VSTRING(final String s, final String lexeme,
                                       final boolean multiline) {
        return multiline ? MSTRING(s, lexeme) : SSTRING(s, lexeme);
    }

    static MockLexicalElements VSTRING(final String s, final boolean multiline) {
        return multiline ? MSTRING(s) : SSTRING(s);
    }

    static MockLexicalElements NL() {
        return SYMBOL("\n");
    }

    static MockLexicalElements SYMBOL(final String s) {
        return new MockLexicalElements(s, LexicalElement.TYPE_SYMBOL);
    }

    /**
     * Checks that the lexicalAnalyzer returns the same lexical elements that are contained in the
     * argument array expectedElements.
     *
     * @param lexicalAnalyzer  the analyzer tested
     * @param expectedElements the lexical elements expected to be returned by the analyzer
     * @throws AnalysisException when the analyzer can not perform the lexical analysis. This itself is a test
     *                           error because this method is to be invoked for input that can be analyzed.
     */
    static void assertLexicals(final LexicalAnalyzer lexicalAnalyzer, final LexicalElement... expectedElements) throws AnalysisException {
        for (final LexicalElement lexicalElement : expectedElements) {
            final LexicalElement element = lexicalAnalyzer.get();
            LOG.info(element.toString());
            thereAreEnoughElements(lexicalElement, element);
            elementsAreOfTheSameType(lexicalElement, element);
            elementsHaveTheSameString(lexicalElement, element);
            elementsHaveTheSameValue(lexicalElement, element);
        }
    }

    private static void elementsHaveTheSameValue(LexicalElement lexicalElement, LexicalElement lexicalElementFromAnalyzer) {
        switch (lexicalElement.getType()) {
            case LexicalElement.TYPE_DOUBLE:
                Assert.assertEquals("different double values",
                        lexicalElement.doubleValue(), lexicalElementFromAnalyzer.doubleValue());
                break;
            case LexicalElement.TYPE_LONG:
                Assert.assertEquals("different long values", lexicalElement.longValue(),
                        lexicalElementFromAnalyzer.longValue());
                break;
            case LexicalElement.TYPE_STRING:
                Assert.assertEquals("different string values",
                        lexicalElement.stringValue(), lexicalElementFromAnalyzer.stringValue());
                break;
            case LexicalElement.TYPE_SYMBOL:
            case LexicalElement.TYPE_IDENTIFIER:
                break;
        }
    }

    private static void elementsHaveTheSameString(LexicalElement lexicalElement, LexicalElement lexicalElementFromAnalyzer) {
        Assert.assertEquals("different lexemes " + lexicalElement.getLexeme() + " vs "
                + lexicalElementFromAnalyzer.getLexeme(), lexicalElement.getLexeme(), lexicalElementFromAnalyzer.getLexeme());
    }

    private static void elementsAreOfTheSameType(LexicalElement lexicalElement, LexicalElement lexicalElementFromAnalyzer) {
        Assert.assertEquals("different types of lexemes " + lexicalElement.getLexeme()
                + " vs " + lexicalElementFromAnalyzer.getLexeme(), lexicalElement.getType(), lexicalElementFromAnalyzer.getType());
    }

    private static void thereAreEnoughElements(LexicalElement lexicalElement, LexicalElement lexicalElementFromAnalyzer) {
        Assert.assertNotNull(
                "there are not enough lexical elements, expecting "
                        + lexicalElement.getLexeme(), lexicalElementFromAnalyzer);
    }

    public static SourceReader createStringReading(final String s) {
        final Reader r = new StringReader(s);
        return new GenericSourceReader(r, null, null);
    }

    static SourceReader createMultiLineStringReading(final String s) {
        return createStringReading("\"\"\"" + s + "\"\"\"");
    }

    static SourceReader createSingleLineStringReading(final String s) {
        return createStringReading("\"" + s + "\"");
    }

    static SourceReader createVStringReading(final String s, final boolean multiline) {
        if (multiline) {
            return createMultiLineStringReading(s);
        } else {
            return createSingleLineStringReading(s);
        }
    }

    static LexicalAnalyzer createStringArrayReading(final String... sources)
            throws IOException {
        assertTrue("there has to be at least one file name and content", sources.length >= 2);
        assertTrue("there should be a content for each 'file name'", sources.length % 2 == 0);
        final StringSourceProvider provider = new StringSourceProvider();
        for (int i = 0; i < sources.length; i++) {
            provider.addSource(sources[i], sources[i + 1]);
            i++;
        }
        return new ScriptBasicLexicalAnalyzer(provider.getSource(sources[0]));
    }
}

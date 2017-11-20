package com.scriptbasic.lexer;

import com.scriptbasic.factories.FactoryFactory;
import com.scriptbasic.interfaces.*;
import com.scriptbasic.readers.GenericHierarchicalReader;
import com.scriptbasic.readers.GenericReader;
import com.scriptbasic.sourceproviders.StringSourceProvider;
import com.scriptbasic.utility.FactoryUtility;
import org.junit.Assert;

import java.io.IOException;
import java.io.StringReader;

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
     * @param expectedElements the lexical elements expected to be returned by the analyzer
     * @param lexicalAnalyzer  the analyzer tested
     * @throws AnalysisException when the analyzer can not perform the lexical analysis. This itself is a test
     *                           error because this method is to be invoked for input that can be analyzed.
     */
    static void assertLexicals(final LexicalElement[] expectedElements,
                               final LexicalAnalyzer lexicalAnalyzer) throws AnalysisException {
        for (final LexicalElement lexicalElement : expectedElements) {
            final LexicalElement lexicalElementFromAnalyzer = lexicalAnalyzer.get();
            thereAreEnoughElements(lexicalElement, lexicalElementFromAnalyzer);
            elementsAreOfTheSameType(lexicalElement, lexicalElementFromAnalyzer);
            elementsHaveTheSameString(lexicalElement, lexicalElementFromAnalyzer);
            elementsHaveTheSameValue(lexicalElement, lexicalElementFromAnalyzer);
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

    public static LexicalAnalyzer createStringReading(final Factory factory,
                                                      final String s) {
        final java.io.Reader r = new StringReader(s);
        final GenericReader reader = new GenericReader();
        reader.set(r);
        reader.setSourceProvider(null);
        reader.set((String) null);
        final LexicalAnalyzer la = FactoryUtility.getLexicalAnalyzer(factory);
        la.set(reader);
        return la;
    }

    static LexicalAnalyzer createMultiLineStringReading(final Factory factory,
                                                        final String s) {
        return createStringReading(factory, "\"\"\"" + s + "\"\"\"");
    }

    static LexicalAnalyzer createSingleLineStringReading(final Factory factory,
                                                         final String s) {
        return createStringReading(factory, "\"" + s + "\"");
    }

    static LexicalAnalyzer createVStringReading(final Factory factory,
                                                final String s, final boolean multiline) {
        if (multiline) {
            return createMultiLineStringReading(factory, s);
        } else {
            return createSingleLineStringReading(factory, s);
        }
    }

    static LexicalAnalyzer createStringArrayReading(final String[] s)
            throws IOException {
        Assert.assertTrue("there has to be at least one file name and content",
                s.length >= 2);
        final StringSourceProvider ssp = new StringSourceProvider();
        for (int i = 0; i < s.length; i++) {
            ssp.addSource(s[i], s[i + 1]);
            i++;
        }

        final Reader reader = ssp.get(s[0]);

        final GenericHierarchicalReader hreader = new GenericHierarchicalReader();
        hreader.include(reader);
        final LexicalAnalyzer la = FactoryFactory.getFactory().get(
                LexicalAnalyzer.class);
        la.set(hreader);
        return la;
    }
}

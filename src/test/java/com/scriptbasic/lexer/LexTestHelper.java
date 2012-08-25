package com.scriptbasic.lexer;

import java.io.IOException;
import java.io.StringReader;

import junit.framework.Assert;

import com.scriptbasic.factories.FactoryFactory;
import com.scriptbasic.interfaces.AnalysisException;
import com.scriptbasic.interfaces.Factory;
import com.scriptbasic.interfaces.LexicalAnalyzer;
import com.scriptbasic.interfaces.LexicalElement;
import com.scriptbasic.interfaces.Reader;
import com.scriptbasic.readers.GenericHierarchicalReader;
import com.scriptbasic.readers.GenericReader;
import com.scriptbasic.sourceproviders.StringSourceProvider;
import com.scriptbasic.utility.FactoryUtility;

public class LexTestHelper {
    static TestLE ID(final String name) {
        return new TestLE(name, LexicalElement.TYPE_IDENTIFIER);
    }

    static TestLE LONG(final String s) {
        final Long l = Long.parseLong(s);
        return new TestLE(s, l);
    }

    static TestLE DOUBLE(final String s) {
        final Double d = Double.parseDouble(s);
        return new TestLE(s, d);
    }

    static TestLE BOOL(final Boolean b) {
        return new TestLE(b.toString(), b);
    }

    static TestLE BOOL(final String b) {
        return new TestLE(b, Boolean.parseBoolean(b));
    }

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

    static TestLE SSTRING(final String s) {
        final String lexeme = sstring2Lexeme(s);
        return SSTRING(s, lexeme);
    }

    static TestLE SSTRING(final String s, final String lexeme) {
        return new TestLE("\"" + lexeme + "\"", s);
    }

    static TestLE MSTRING(final String s) {
        final String lexeme = string2Lexeme(s);
        return MSTRING(s, lexeme);
    }

    static TestLE MSTRING(final String s, final String lexeme) {
        return new TestLE("\"\"\"" + lexeme + "\"\"\"", s);
    }

    static TestLE VSTRING(final String s, final String lexeme,
            final boolean multiline) {
        return multiline ? MSTRING(s, lexeme) : SSTRING(s, lexeme);
    }

    static TestLE VSTRING(final String s, final boolean multiline) {
        return multiline ? MSTRING(s) : SSTRING(s);
    }

    static TestLE NL() {
        return SYMBOL("\n");
    }

    static TestLE SYMBOL(final String s) {
        return new TestLE(s, LexicalElement.TYPE_SYMBOL);
    }

    static void assertLexicals(final LexicalElement[] lea,
            final LexicalAnalyzer la) throws AnalysisException {
        for (final LexicalElement le : lea) {
            final LexicalElement le1 = la.get();
            Assert.assertNotNull(
                    "there are not enough lexical elements, expecting "
                            + le.getLexeme(), le1);
            Assert.assertEquals("different types of lexemes " + le.getLexeme()
                    + " vs " + le1.getLexeme(), le.getType(), le1.getType());
            Assert.assertEquals("different lexemes " + le.getLexeme() + " vs "
                    + le1.getLexeme(), le.getLexeme(), le1.getLexeme());
            switch (le.getType()) {
            case LexicalElement.TYPE_DOUBLE:
                Assert.assertEquals("different double values",
                        le.doubleValue(), le1.doubleValue());
                break;
            case LexicalElement.TYPE_LONG:
                Assert.assertEquals("different long values", le.longValue(),
                        le1.longValue());
                break;
            case LexicalElement.TYPE_STRING:
                Assert.assertEquals("different string values",
                        le.stringValue(), le1.stringValue());
                break;
            case LexicalElement.TYPE_SYMBOL:
            case LexicalElement.TYPE_IDENTIFIER:
                break;
            }
        }
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

    static LexicalAnalyzer createMStringReading(final Factory factory,
            final String s) {
        return createStringReading(factory, "\"\"\"" + s + "\"\"\"");
    }

    static LexicalAnalyzer createSStringReading(final Factory factory,
            final String s) {
        return createStringReading(factory, "\"" + s + "\"");
    }

    static LexicalAnalyzer createVStringReading(final Factory factory,
            final String s, final boolean multiline) {
        if (multiline) {
            return createMStringReading(factory, s);
        } else {
            return createSStringReading(factory, s);
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

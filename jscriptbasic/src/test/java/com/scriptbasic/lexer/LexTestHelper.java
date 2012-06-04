package com.scriptbasic.lexer;

import java.io.IOException;
import java.io.StringReader;

import junit.framework.TestCase;

import com.scriptbasic.interfaces.LexicalAnalyzer;
import com.scriptbasic.interfaces.LexicalElement;
import com.scriptbasic.interfaces.LexicalException;
import com.scriptbasic.interfaces.Reader;
import com.scriptbasic.readers.GenericHierarchicalReader;
import com.scriptbasic.readers.GenericReader;
import com.scriptbasic.sourceproviders.StringSourceProvider;

public class LexTestHelper {
    static TestLE ID(final String name) {
        return new TestLE(name, LexicalElement.TYPE_IDENTIFIER);
    }

    static TestLE LONG(final String s) {
        Long l = Long.parseLong(s);
        return new TestLE(s, l);
    }

    static TestLE DOUBLE(final String s) {
        Double d = Double.parseDouble(s);
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

    static TestLE SSTRING(String s) {
        String lexeme = sstring2Lexeme(s);
        return SSTRING(s, lexeme);
    }

    static TestLE SSTRING(String s, String lexeme) {
        return new TestLE("\"" + lexeme + "\"", s);
    }

    static TestLE MSTRING(String s) {
        String lexeme = string2Lexeme(s);
        return MSTRING(s, lexeme);
    }

    static TestLE MSTRING(String s, String lexeme) {
        return new TestLE("\"\"\"" + lexeme + "\"\"\"", s);
    }

    static TestLE VSTRING(String s, String lexeme, boolean multiline) {
        return multiline ? MSTRING(s, lexeme) : SSTRING(s, lexeme);
    }

    static TestLE VSTRING(String s, boolean multiline) {
        return multiline ? MSTRING(s) : SSTRING(s);
    }

    static TestLE NL() {
        return SYMBOL("\n");
    }

    static TestLE SYMBOL(String s) {
        return new TestLE(s, LexicalElement.TYPE_SYMBOL);
    }

    static void assertLexicals(LexicalElement[] lea, LexicalAnalyzer la)
            throws LexicalException {
        for (LexicalElement le : lea) {
            LexicalElement le1 = la.get();
            TestCase.assertNotNull(
                    "there are not enough lexical elements, expecting "
                            + le.get(), le1);
            TestCase.assertEquals("different types of lexemes " + le.get()
                    + " vs " + le1.get(), le.type(), le1.type());
            TestCase.assertEquals("different lexemes " + le.get() + " vs "
                    + le1.get(), le.get(), le1.get());
            switch (le.type()) {
            case LexicalElement.TYPE_DOUBLE:
                TestCase.assertEquals("different double values",
                        le.doubleValue(), le1.doubleValue());
                break;
            case LexicalElement.TYPE_LONG:
                TestCase.assertEquals("different long values", le.longValue(),
                        le1.longValue());
                break;
            case LexicalElement.TYPE_STRING:
                TestCase.assertEquals("different string values",
                        le.stringValue(), le1.stringValue());
                break;
            case LexicalElement.TYPE_SYMBOL:
            case LexicalElement.TYPE_IDENTIFIER:
                break;
            }
        }
    }

    public static LexicalAnalyzer createStringReading(String s) {
        java.io.Reader r = new StringReader(s);
        GenericReader reader = new GenericReader();
        reader.set(r);
        reader.setSourceProvider(null);
        reader.set((String) null);
        LexicalAnalyzer la = new ScriptBasicLexicalAnalyzer();
        la.set(reader);
        return la;
    }

    static LexicalAnalyzer createMStringReading(String s) {
        return createStringReading("\"\"\"" + s + "\"\"\"");
    }

    static LexicalAnalyzer createSStringReading(String s) {
        return createStringReading("\"" + s + "\"");
    }

    static LexicalAnalyzer createVStringReading(String s, boolean multiline) {
        if (multiline) {
            return createMStringReading(s);
        } else {
            return createSStringReading(s);
        }
    }

    static LexicalAnalyzer createStringArrayReading(String[] s)
            throws IOException {
        TestCase.assertTrue(
                "there has to be at least one file name and content",
                s.length >= 2);
        StringSourceProvider ssp = new StringSourceProvider();
        for (int i = 0; i < s.length; i++) {
            ssp.addSource(s[i], s[i + 1]);
            i++;
        }

        Reader reader = ssp.get(s[0]);

        GenericHierarchicalReader hreader = new GenericHierarchicalReader();
        hreader.include(reader);

        LexicalAnalyzer la = new ScriptBasicLexicalAnalyzer();
        la.set(hreader);
        return la;
    }
}

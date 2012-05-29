package com.scriptbasic.lexer;

import static com.scriptbasic.lexer.LexTestHelper.DOUBLE;
import static com.scriptbasic.lexer.LexTestHelper.ID;
import static com.scriptbasic.lexer.LexTestHelper.LONG;
import static com.scriptbasic.lexer.LexTestHelper.MSTRING;
import static com.scriptbasic.lexer.LexTestHelper.SSTRING;
import static com.scriptbasic.lexer.LexTestHelper.SYMBOL;
import static com.scriptbasic.lexer.LexTestHelper.VSTRING;
import static com.scriptbasic.lexer.LexTestHelper.assertLexicals;
import static com.scriptbasic.lexer.LexTestHelper.createStringReading;
import static com.scriptbasic.lexer.LexTestHelper.createVStringReading;
import junit.framework.TestCase;

import com.scriptbasic.interfaces.LexicalElement;
import com.scriptbasic.interfaces.LexicalException;

public class TestBasicLexicalAnalyzer extends TestCase {
    public TestBasicLexicalAnalyzer(String testName) {
        super(testName);
    }

    private void keywordtest(String s) throws LexicalException {
        assertLexicals(new LexicalElement[] { SYMBOL(s) },
                createStringReading(s));
    }

    public void testKeywords() throws LexicalException {
        keywordtest("for");
        keywordtest("end");
        keywordtest("next");
        keywordtest("let");
        keywordtest("if");
        keywordtest("while");
        keywordtest("wend");
        keywordtest("else");
        keywordtest("elseif");
        keywordtest("repeat");
        keywordtest("until");
    }

    public void testString() throws LexicalException {
        boolean multiline = false;
        do {
            // empty string
            assertLexicals(new LexicalElement[] { VSTRING("", multiline) },
                    createVStringReading("", multiline));
            // string with a " in it
            assertLexicals(
                    new LexicalElement[] { VSTRING("\"", "\\\"", multiline) },
                    createVStringReading("\\\"", multiline));
            // string with a " and a space without escaping the "
            // works only in multi-line string
            if (multiline) {
                assertLexicals(
                        new LexicalElement[] { VSTRING("\" ", "\" ", multiline) },
                        createVStringReading("\" ", multiline));
            }
            // string with a " and a space escaping the "
            assertLexicals(
                    new LexicalElement[] { VSTRING("\" ", "\\\" ", multiline) },
                    createVStringReading("\\\" ", multiline));
            // string with a new line in it
            assertLexicals(
                    new LexicalElement[] { VSTRING("\n", "\\n", multiline) },
                    createVStringReading("\\n", multiline));
            // one character and a new line in it
            assertLexicals(
                    new LexicalElement[] { VSTRING("a\n", "a\\n", multiline) },
                    createVStringReading("a\\n", multiline));
            // string with a lf in it
            assertLexicals(
                    new LexicalElement[] { VSTRING("\r", "\\r", multiline) },
                    createVStringReading("\\r", multiline));
            // string with a tab in it
            assertLexicals(
                    new LexicalElement[] { VSTRING("\t", "\\t", multiline) },
                    createVStringReading("\\t", multiline));
            // string with a backslash in it
            assertLexicals(new LexicalElement[] { VSTRING("\\", multiline) },
                    createVStringReading("\\\\", multiline));
            // string with a character not needing escape
            assertLexicals(
                    new LexicalElement[] { VSTRING("R", "\\R", multiline) },
                    createVStringReading("\\R", multiline));
            // string with a single normal character in it
            assertLexicals(new LexicalElement[] { VSTRING("x", multiline) },
                    createVStringReading("x", multiline));

            // try multi-line and then exit
            multiline = !multiline;
        } while (multiline);

        assertLexicals(
                new LexicalElement[] { MSTRING("1\"\"\"\n2", "1\\\"\"\"\\n2") },
                createStringReading("\"\"\"1\\\"\"\"\\n2\"\"\""));
        assertLexicals(new LexicalElement[] { MSTRING("1\n2") },
                createStringReading("\"\"\"1\\n2\"\"\""));
        assertLexicals(new LexicalElement[] { MSTRING("\\") },
                createStringReading("\"\"\"\\\\\"\"\""));
        assertLexicals(new LexicalElement[] { MSTRING("1\"\n2") },
                createStringReading("\"\"\"1\"\\n2\"\"\""));
        assertLexicals(new LexicalElement[] { MSTRING("1\"\"\n2") },
                createStringReading("\"\"\"1\"\"\\n2\"\"\""));
    }

    public void testNewLine() throws LexicalException {
        assertLexicals(new LexicalElement[] { SYMBOL("\n") },
                createStringReading("\n"));
    }

    public void testFloatNumber() throws LexicalException {
        assertLexicals(new LexicalElement[] { DOUBLE("13e3") },
                createStringReading("13e3"));
        assertLexicals(new LexicalElement[] { DOUBLE("13.8") },
                createStringReading("13.8"));
        assertLexicals(new LexicalElement[] { DOUBLE("13.8e2") },
                createStringReading("13.8e2"));
        assertLexicals(new LexicalElement[] { DOUBLE("13.8e+2") },
                createStringReading("13.8e+2"));
        assertLexicals(new LexicalElement[] { DOUBLE("13.8e-2") },
                createStringReading("13.8e-2"));
        assertLexicals(new LexicalElement[] { DOUBLE("13.8E2") },
                createStringReading("13.8E2"));
        assertLexicals(new LexicalElement[] { DOUBLE("13.8E+2") },
                createStringReading("13.8E+2"));
        assertLexicals(new LexicalElement[] { DOUBLE("13.8E-2") },
                createStringReading("13.8E-2"));
    }

    public void testFloatAndSomething() throws LexicalException {
        assertLexicals(new LexicalElement[] { DOUBLE("13.2"), ID("e"),
                SYMBOL("+") }, createStringReading("13.2e+"));
        assertLexicals(new LexicalElement[] { DOUBLE("13.2"), ID("e") },
                createStringReading("13.2e"));
        assertLexicals(new LexicalElement[] { DOUBLE("13.2"), ID("E"),
                SYMBOL("+") }, createStringReading("13.2E+"));
        assertLexicals(new LexicalElement[] { DOUBLE("13.2"), ID("E") },
                createStringReading("13.2E"));
        assertLexicals(new LexicalElement[] { LONG("13"), SYMBOL(".") },
                createStringReading("13."));
        assertLexicals(new LexicalElement[] { LONG("13"), SYMBOL("\n") },
                createStringReading("13\n"));
    }

    public void testUnterminatedString() throws LexicalException {
        try {
            assertLexicals(
                    new LexicalElement[] { SSTRING("justAnything, should not check it, if it fails the test fails") },
                    createStringReading("\""));
        } catch (UnterminatedStringException use) {
            // this is what we expect
        }
    }

    public void testSpaceSeparated() throws LexicalException {
        assertLexicals(
                new LexicalElement[] { ID("alma"), LONG("123"), ID("kšrte"),
                        SYMBOL("<<"), SYMBOL(">="), SYMBOL("<<"),
                        DOUBLE("12.3"), DOUBLE("13e3"), DOUBLE("12.3e2"),
                        SSTRING("habakukk"), SYMBOL("<") },
                createStringReading("alma 123 kšrte << >= << 12.3 13e3 12.3e2 \"habakukk\" <"));
    }
}

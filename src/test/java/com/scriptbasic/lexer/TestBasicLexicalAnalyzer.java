package com.scriptbasic.lexer;

import com.scriptbasic.exceptions.UnterminatedStringException;
import com.scriptbasic.factories.SingletonFactoryFactory;
import com.scriptbasic.interfaces.AnalysisException;
import com.scriptbasic.interfaces.Factory;
import com.scriptbasic.interfaces.LexicalAnalyzer;
import com.scriptbasic.interfaces.LexicalElement;
import org.junit.Test;

import static com.scriptbasic.lexer.LexTestHelper.*;

public class TestBasicLexicalAnalyzer {
    private Factory factory = SingletonFactoryFactory.getFactory();

    private void keywordtest(final String s) throws AnalysisException {
        assertLexicals(new LexicalElement[]{SYMBOL(s)},
                createStringReading(s));
    }

    private LexicalAnalyzer createVStringReading(String s, boolean multiline) {
        return LexTestHelper.createVStringReading(factory, s, multiline);
    }

    private LexicalAnalyzer createStringReading(String s) {
        return LexTestHelper.createStringReading(factory, s);
    }

    @Test
    public void keywordsAreRecognizedAsSymbols() throws AnalysisException {
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

    @Test
    public void differentStringsAreAnalyzedNicely() throws AnalysisException {
        boolean multiline = false;
        do {
            // empty string
            assertLexicals(new LexicalElement[]{VSTRING("", multiline)},
                    createVStringReading("", multiline));
            // string with a " in it
            assertLexicals(
                    new LexicalElement[]{VSTRING("\"", "\\\"", multiline)},
                    createVStringReading("\\\"", multiline));
            // string with a " and a space without escaping the "
            // works only in multi-line string
            if (multiline) {
                assertLexicals(
                        new LexicalElement[]{VSTRING("\" ", "\" ", multiline)},
                        createVStringReading("\" ", multiline));
            }
            // string with a " and a space escaping the "
            assertLexicals(
                    new LexicalElement[]{VSTRING("\" ", "\\\" ", multiline)},
                    createVStringReading("\\\" ", multiline));
            // string with a new line in it
            assertLexicals(
                    new LexicalElement[]{VSTRING("\n", "\\n", multiline)},
                    createVStringReading("\\n", multiline));
            // one character and a new line in it
            assertLexicals(
                    new LexicalElement[]{VSTRING("a\n", "a\\n", multiline)},
                    createVStringReading("a\\n", multiline));
            // string with a lf in it
            assertLexicals(
                    new LexicalElement[]{VSTRING("\r", "\\r", multiline)},
                    createVStringReading("\\r", multiline));
            // string with a tab in it
            assertLexicals(
                    new LexicalElement[]{VSTRING("\t", "\\t", multiline)},
                    createVStringReading("\\t", multiline));
            // string with a backslash in it
            assertLexicals(new LexicalElement[]{VSTRING("\\", multiline)},
                    createVStringReading("\\\\", multiline));
            // string with a character not needing escape
            assertLexicals(
                    new LexicalElement[]{VSTRING("R", "\\R", multiline)},
                    createVStringReading("\\R", multiline));
            // string with a single normal character in it
            assertLexicals(new LexicalElement[]{VSTRING("x", multiline)},
                    createVStringReading("x", multiline));

            // try multi-line and then exit
            multiline = !multiline;
        } while (multiline);

        assertLexicals(
                new LexicalElement[]{MSTRING("1\"\"\"\n2", "1\\\"\"\"\\n2")},
                createStringReading("\"\"\"1\\\"\"\"\\n2\"\"\""));
        assertLexicals(new LexicalElement[]{MSTRING("1\n2")},
                createStringReading("\"\"\"1\\n2\"\"\""));
        assertLexicals(new LexicalElement[]{MSTRING("\\")},
                createStringReading("\"\"\"\\\\\"\"\""));
        assertLexicals(new LexicalElement[]{MSTRING("1\"\n2")},
                createStringReading("\"\"\"1\"\\n2\"\"\""));
        assertLexicals(new LexicalElement[]{MSTRING("1\"\"\n2")},
                createStringReading("\"\"\"1\"\"\\n2\"\"\""));
    }

    @Test
    public void newLineIsAnalyzedAs_surprise_surprise_newLine() throws AnalysisException {
        assertLexicals(new LexicalElement[]{SYMBOL("\n")},
                createStringReading("\n"));
    }

    @Test
    public void integerNumbersAreAnalyzedNicely() throws AnalysisException {
        assertLexicals(new LexicalElement[]{LONG("12")},
                createStringReading("12"));
    }

    @Test
    public void floatingNumbersAreAnalyzedNicely() throws AnalysisException {
        assertLexicals(new LexicalElement[]{DOUBLE("13e3")},
                createStringReading("13e3"));
        assertLexicals(new LexicalElement[]{DOUBLE("13.8")},
                createStringReading("13.8"));
        assertLexicals(new LexicalElement[]{DOUBLE("13.8e2")},
                createStringReading("13.8e2"));
        assertLexicals(new LexicalElement[]{DOUBLE("13.8e+2")},
                createStringReading("13.8e+2"));
        assertLexicals(new LexicalElement[]{DOUBLE("13.8e-2")},
                createStringReading("13.8e-2"));
        assertLexicals(new LexicalElement[]{DOUBLE("13.8E2")},
                createStringReading("13.8E2"));
        assertLexicals(new LexicalElement[]{DOUBLE("13.8E+2")},
                createStringReading("13.8E+2"));
        assertLexicals(new LexicalElement[]{DOUBLE("13.8E-2")},
                createStringReading("13.8E-2"));
    }

    @Test
    public void booleanConstantsWithDifferentCasingsWorkNicely() throws AnalysisException {
        assertLexicals(new LexicalElement[]{BOOL(true), BOOL(false)},
                createStringReading("true false"));
        assertLexicals(new LexicalElement[]{BOOL("TRUE"), BOOL("FALSE")},
                createStringReading("TRUE FALSE"));
        assertLexicals(new LexicalElement[]{BOOL("True"), BOOL("False")},
                createStringReading("True False"));
        assertLexicals(new LexicalElement[]{BOOL("tRUe"), BOOL("fALse")},
                createStringReading("tRUe fALse"));
        assertLexicals(new LexicalElement[]{BOOL("trUe"), BOOL("faLse")},
                createStringReading("trUe faLse"));
        assertLexicals(new LexicalElement[]{BOOL("TrUe"), BOOL("fAlSe")},
                createStringReading("TrUe fAlSe"));
    }

    @Test
    public void floatinNumbersAndSomethingAfterwardDoesNotCauseProblem() throws AnalysisException {
        assertLexicals(new LexicalElement[]{DOUBLE("13.2"), ID("e"),
                SYMBOL("+")}, createStringReading("13.2e+"));
        assertLexicals(new LexicalElement[]{DOUBLE("13.2"), ID("e")},
                createStringReading("13.2e"));
        assertLexicals(new LexicalElement[]{DOUBLE("13.2"), ID("E"),
                SYMBOL("+")}, createStringReading("13.2E+"));
        assertLexicals(new LexicalElement[]{DOUBLE("13.2"), ID("E")},
                createStringReading("13.2E"));
        assertLexicals(new LexicalElement[]{LONG("13"), SYMBOL(".")},
                createStringReading("13."));
        assertLexicals(new LexicalElement[]{LONG("13"), SYMBOL("\n")},
                createStringReading("13\n"));
    }

    @Test(expected = UnterminatedStringException.class)
    public void unterminatedStringThrowsException() throws AnalysisException {
        assertLexicals(
                new LexicalElement[]{SSTRING("justAnything, should not check it, if it does it fails the test")},
                createStringReading("\""));
    }

    @Test
    public void spaceSeparatedTerminalsAreAnalyzedNicely() throws AnalysisException {
        assertLexicals(
                new LexicalElement[]{ID("alma"), LONG("123"), ID("körte"),
                        SYMBOL("<="), SYMBOL(">="), SYMBOL("<="),
                        DOUBLE("12.3"), DOUBLE("13e3"), DOUBLE("12.3e2"),
                        SSTRING("habakukk"), SYMBOL("<")},
                createStringReading("alma 123 körte <= >= <= 12.3 13e3 12.3e2 \"habakukk\" <"));
    }
}

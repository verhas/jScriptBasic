package com.scriptbasic.lexer;

import com.scriptbasic.exceptions.UnterminatedStringException;
import com.scriptbasic.interfaces.AnalysisException;
import com.scriptbasic.interfaces.LexicalAnalyzer;
import com.scriptbasic.api.SourceReader;
import com.scriptbasic.lexer.elements.ScriptBasicLexicalAnalyzer;
import org.junit.Test;

import static com.scriptbasic.lexer.LexTestHelper.*;

public class TestBasicLexicalAnalyzer {

    private LexicalAnalyzer from(String s) {
        return new ScriptBasicLexicalAnalyzer(createStringReading(s));
    }

    private void keywordtest(final String s) throws AnalysisException {
        assertLexicals(new ScriptBasicLexicalAnalyzer(createStringReading(s)), SYMBOL(s));
    }

    private SourceReader createVStringReading(String s, boolean multiline) {
        return LexTestHelper.createVStringReading(s, multiline);
    }

    private SourceReader createStringReading(String s) {
        return LexTestHelper.createStringReading(s);
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
            assertLexicals(new ScriptBasicLexicalAnalyzer(createVStringReading("", multiline)), VSTRING("", multiline)
            );
            // string with a " in it
            assertLexicals(new ScriptBasicLexicalAnalyzer(createVStringReading("\\\"", multiline)), VSTRING("\"", "\\\"", multiline)
            );
            // string with a " and a space without escaping the "
            // works only in multi-line string
            if (multiline) {
                assertLexicals(new ScriptBasicLexicalAnalyzer(createVStringReading("\" ", true)), VSTRING("\" ", "\" ", true));
            }
            // string with a " and a space escaping the "
            assertLexicals(new ScriptBasicLexicalAnalyzer(createVStringReading("\\\" ", multiline)), VSTRING("\" ", "\\\" ", multiline)
            );
            // string with a new line in it
            assertLexicals(new ScriptBasicLexicalAnalyzer(createVStringReading("\\n", multiline)), VSTRING("\n", "\\n", multiline)
            );
            // one character and a new line in it
            assertLexicals(new ScriptBasicLexicalAnalyzer(createVStringReading("a\\n", multiline)), VSTRING("a\n", "a\\n", multiline)
            );
            // string with a lf in it
            assertLexicals(new ScriptBasicLexicalAnalyzer(createVStringReading("\\r", multiline)), VSTRING("\r", "\\r", multiline)
            );
            // string with a tab in it
            assertLexicals(new ScriptBasicLexicalAnalyzer(createVStringReading("\\t", multiline)), VSTRING("\t", "\\t", multiline)
            );
            // string with a backslash in it
            assertLexicals(new ScriptBasicLexicalAnalyzer(createVStringReading("\\\\", multiline)), VSTRING("\\", multiline)
            );
            // string with a character not needing escape
            assertLexicals(new ScriptBasicLexicalAnalyzer(
                    createVStringReading("\\R", multiline)), VSTRING("R", "\\R", multiline)
            );
            // string with a single normal character in it
            assertLexicals(new ScriptBasicLexicalAnalyzer(createVStringReading("x", multiline)), VSTRING("x", multiline)
            );

            // try multi-line and then exit
            multiline = !multiline;
        } while (multiline);

        assertLexicals(new ScriptBasicLexicalAnalyzer(
                createStringReading("\"\"\"1\\\"\"\"\\n2\"\"\"")), MSTRING("1\"\"\"\n2", "1\\\"\"\"\\n2")
        );
        assertLexicals(from("\"\"\"1\\n2\"\"\""), MSTRING("1\n2")
        );
        assertLexicals(from("\"\"\"\\\\\"\"\""), MSTRING("\\")
        );
        assertLexicals(from("\"\"\"1\"\\n2\"\"\""), MSTRING("1\"\n2")
        );
        assertLexicals(from("\"\"\"1\"\"\\n2\"\"\""), MSTRING("1\"\"\n2")
        );
    }

    @Test
    public void newLineIsAnalyzedAs_surprise_surprise_newLine() throws AnalysisException {
        assertLexicals(from("\n"), SYMBOL("\n")
        );
    }

    @Test
    public void integerNumbersAreAnalyzedNicely() throws AnalysisException {
        assertLexicals(from("12"), LONG("12")
        );
    }

    @Test
    public void floatingNumbersAreAnalyzedNicely() throws AnalysisException {
        assertLexicals(from("13e3"), DOUBLE("13e3")
        );
        assertLexicals(from("13.8"), DOUBLE("13.8")
        );
        assertLexicals(from("13.8e2"), DOUBLE("13.8e2"));
        assertLexicals(from("13.8e+2"), DOUBLE("13.8e+2"));
        assertLexicals(from("13.8e-2"), DOUBLE("13.8e-2"));
        assertLexicals(from("13.8E2"), DOUBLE("13.8E2"));
        assertLexicals(from("13.8E+2"), DOUBLE("13.8E+2"));
        assertLexicals(from("13.8E-2"), DOUBLE("13.8E-2"));
    }

    @Test
    public void booleanConstantsWithDifferentCasingsWorkNicely() throws AnalysisException {
        assertLexicals(from("true false"), BOOL(true), BOOL(false)
        );
        assertLexicals(from("TRUE FALSE"), BOOL("TRUE"), BOOL("FALSE"));
        assertLexicals(from("True False"), BOOL("True"), BOOL("False"));
        assertLexicals(from("tRUe fALse"), BOOL("tRUe"), BOOL("fALse"));
        assertLexicals(from("trUe faLse"), BOOL("trUe"), BOOL("faLse"));
        assertLexicals(from("TrUe fAlSe"), BOOL("TrUe"), BOOL("fAlSe"));
    }

    @Test
    public void floatinNumbersAndSomethingAfterwardDoesNotCauseProblem() throws AnalysisException {
        assertLexicals(from("13.2e+"), DOUBLE("13.2"), ID("e"), SYMBOL("+"));
        assertLexicals(from("13.2e"), DOUBLE("13.2"), ID("e"));
        assertLexicals(from("13.2E+"), DOUBLE("13.2"), ID("E"), SYMBOL("+"));
        assertLexicals(from("13.2E"), DOUBLE("13.2"), ID("E"));
        assertLexicals(from("13."), LONG("13"), SYMBOL("."));
        assertLexicals(from("13\n"), LONG("13"), SYMBOL("\n"));
    }

    @Test(expected = UnterminatedStringException.class)
    public void unterminatedStringThrowsException() throws AnalysisException {
        assertLexicals(new ScriptBasicLexicalAnalyzer(
                createStringReading("\"")), SSTRING("justAnything, should not check it, if it does it fails the test")
        );
    }

    @Test
    public void spaceSeparatedTerminalsAreAnalyzedNicely() throws AnalysisException {
        assertLexicals(new ScriptBasicLexicalAnalyzer(
                        createStringReading("alma 123 körte <= >= <= 12.3 13e3 12.3e2 \"habakukk\" <")),
                ID("alma"), LONG("123"), ID("körte"),
                SYMBOL("<="), SYMBOL(">="), SYMBOL("<="),
                DOUBLE("12.3"), DOUBLE("13e3"), DOUBLE("12.3e2"),
                SSTRING("habakukk"), SYMBOL("<"));
    }
}

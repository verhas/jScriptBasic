package com.scriptbasic.lexer;

import static com.scriptbasic.lexer.LexTestHelper.ID;
import static com.scriptbasic.lexer.LexTestHelper.NL;
import static com.scriptbasic.lexer.LexTestHelper.SSTRING;
import static com.scriptbasic.lexer.LexTestHelper.SYMBOL;
import static com.scriptbasic.lexer.LexTestHelper.assertLexicals;
import static com.scriptbasic.lexer.LexTestHelper.createStringArrayReading;

import java.io.IOException;
import java.util.ArrayList;

import junit.framework.TestCase;

import com.scriptbasic.interfaces.AnalysisException;
import com.scriptbasic.interfaces.LexicalElement;

public class TestHierarchicalReader extends TestCase {
    public TestHierarchicalReader(final String testName) {
        super(testName);
    }

    @SuppressWarnings("static-method")
    public void testOneInclude() throws AnalysisException, IOException {
        assertLexicals(new LexicalElement[] { ID("identifier"), SYMBOL("\n"),
                SYMBOL("<"), SYMBOL("\n"), SSTRING("string") },
                createStringArrayReading(new String[] { "main",
                        "identifier\ninclude \"sub1\"\n\"string\"", "sub1",
                        "<\n" }));
    }

    @SuppressWarnings("static-method")
    public void testFileNotFound() throws AnalysisException, IOException {
        try {
            assertLexicals(new LexicalElement[] { ID("identifier"),
                    SYMBOL("\n"), SYMBOL("<<<"), SYMBOL("\n"),
                    SSTRING("string") }, createStringArrayReading(new String[] {
                    "main", "identifier\ninclude \"nonexistent\"\n\"string\"",
                    "sub1", "<<<\n" }));
            assertTrue("Code should not get here", false);
        } catch (final AnalysisException lex) {
            // OK
        }
    }

    private void addX(final ArrayList<LexicalElement> lexes,
            final ArrayList<String> files, final int level) {
        files.add("file" + level);
        if (level == 0) {
            files.add("file" + level + "\nend\n");
            lexes.add(ID("file" + level));
            lexes.add(NL());
            lexes.add(SYMBOL("end"));
            lexes.add(NL());
        } else {
            files.add("file" + level + "\ninclude \"file" + (level - 1)
                    + "\"\nend\n");
            lexes.add(ID("file" + level));
            lexes.add(NL());
            addX(lexes, files, level - 1);
            lexes.add(SYMBOL("end"));
            lexes.add(NL());
        }
    }

    public void testMultiInclude() throws AnalysisException, IOException {
        final ArrayList<LexicalElement> lexes = new ArrayList<LexicalElement>();
        final ArrayList<String> files = new ArrayList<String>();
        final int level = 20;
        addX(lexes, files, level);
        assertLexicals(
                lexes.toArray(new LexicalElement[lexes.size()]),
                createStringArrayReading(files.toArray(new String[files.size()])));
    }

    @SuppressWarnings("static-method")
    public void testCircularReference() throws AnalysisException, IOException {
        final ArrayList<LexicalElement> lexes = new ArrayList<LexicalElement>();
        final ArrayList<String> files = new ArrayList<String>();
        files.add("foo");
        files.add("include \"bar\"\n");
        files.add("bar");
        files.add("include \"foo\"\n");
        lexes.add(ID(""));
        try {
            assertLexicals(lexes.toArray(new LexicalElement[lexes.size()]),
                    createStringArrayReading(files.toArray(new String[files
                            .size()])));
            assertTrue("Circular reference did not throw error", false);
        } catch (final AnalysisException lex) {
            // this is ok
        }
    }
}

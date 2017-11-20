package com.scriptbasic.lexer;

import com.scriptbasic.exceptions.GenericSyntaxException;
import com.scriptbasic.interfaces.AnalysisException;
import com.scriptbasic.interfaces.LexicalElement;
import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;

import static com.scriptbasic.lexer.LexTestHelper.*;
import static org.junit.Assert.assertTrue;

public class TestHierarchicalReader {

    @Test
    public void testOneInclude() throws AnalysisException, IOException {
        assertLexicals(new LexicalElement[]{ID("identifier"), SYMBOL("\n"),
                        SYMBOL("<"), SYMBOL("\n"), SSTRING("string")},
                createStringArrayReading(new String[]{"main",
                        "identifier\ninclude \"sub1\"\n\"string\"", "sub1",
                        "<\n"}));
    }

    @Test(expected = GenericSyntaxException.class)
    public void testExtraCharsAfterInclude() throws AnalysisException,
            IOException {
        assertLexicals(
                new LexicalElement[]{ID("identifier"), SYMBOL("\n"),
                        SYMBOL("<"), SYMBOL("\n"), SSTRING("string")},
                createStringArrayReading(new String[]{
                        "main",
                        "identifier\ninclude \"sub1\" bla bla \n\"string\"",
                        "sub1", "<\n"}));
    }

    @Test
    public void testFileNotFound() throws AnalysisException, IOException {
        try {
            assertLexicals(new LexicalElement[]{ID("identifier"),
                    SYMBOL("\n"), SYMBOL("<<<"), SYMBOL("\n"),
                    SSTRING("string")}, createStringArrayReading(new String[]{
                    "main", "identifier\ninclude \"nonexistent\"\n\"string\"",
                    "sub1", "<<<\n"}));
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

    @Test
    public void testMultiInclude() throws AnalysisException, IOException {
        final ArrayList<LexicalElement> lexes = new ArrayList<>();
        final ArrayList<String> files = new ArrayList<>();
        final int level = 20;
        addX(lexes, files, level);
        assertLexicals(
                lexes.toArray(new LexicalElement[lexes.size()]),
                createStringArrayReading(files.toArray(new String[files.size()])));
    }

    @Test
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

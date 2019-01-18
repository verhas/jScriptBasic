package com.scriptbasic.lexer;

import com.scriptbasic.interfaces.AnalysisException;
import com.scriptbasic.interfaces.BasicRuntimeException;
import com.scriptbasic.interfaces.BasicSyntaxException;
import com.scriptbasic.interfaces.LexicalElement;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.ArrayList;

import static com.scriptbasic.lexer.LexTestHelper.*;
import static org.junit.jupiter.api.Assertions.*;

public class TestHierarchicalReader {

    @Test
    public void testOneInclude() throws AnalysisException, IOException {
        assertLexicals(createStringArrayReading("main",
                "identifier\ninclude \"sub1\"\n\"string\"", "sub1",
                "<\n"), ID("identifier"), SYMBOL("\n"),
                SYMBOL("<"), SYMBOL("\n"), SSTRING("string"));
    }

    @Test()
    public void testExtraCharsAfterInclude() throws AnalysisException,
            IOException {
        Assertions.assertThrows(BasicSyntaxException.class, () ->
        assertLexicals(
                createStringArrayReading(
                        "main",
                        "identifier\ninclude \"sub1\" bla bla \n\"string\"",
                        "sub1", "<\n"),
                ID("identifier"), SYMBOL("\n"),
                SYMBOL("<"), SYMBOL("\n"), SSTRING("string")));
    }

    @Test()
    public void testFileNotFound() throws AnalysisException, IOException {
        Assertions.assertThrows(AnalysisException.class, () ->
        assertLexicals(createStringArrayReading(
                "main", "identifier\ninclude \"nonexistent\"\n\"string\"",
                "sub1", "<<<\n"), ID("identifier"),
                SYMBOL("\n"), SYMBOL("<<<"), SYMBOL("\n"),
                SSTRING("string")));
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

    @SuppressWarnings("ToArrayCallWithZeroLengthArrayArgument")
    @Test
    public void testMultiInclude() throws AnalysisException, IOException {
        final ArrayList<LexicalElement> lexes = new ArrayList<>();
        final ArrayList<String> files = new ArrayList<>();
        final var level = 20;
        addX(lexes, files, level);
        assertLexicals(
                createStringArrayReading(files.toArray(new String[files.size()])), lexes.toArray(new LexicalElement[lexes.size()])
        );
    }

    @Test()
    public void testCircularReference() throws AnalysisException, IOException {
        Assertions.assertThrows(AnalysisException.class, () ->
        assertLexicals(createStringArrayReading("foo", "include \"bar\"", "bar", "include \"foo\""),
                ID("")));
    }
}

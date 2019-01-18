package com.scriptbasic.sourceproviders;

import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

public class TestFileSourceProvider {

    private static final String tempDir = System.getProperty("java.io.tmpdir");
    private static final String ps = File.separator;
    private static final String testFileName = "testFileName";
    private static final String testStringToFile = "hallo hallo";

    @Test
    public void testFSP() throws IOException {
        final var file = createTemporaryTestFile();
        try {
            // get the test file
            final var fsp = new FileSourceProvider();
            fsp.setSourcePath(new BasicSourcePath());
            fsp.getSourcePath().add(tempDir + ps + "abrakadabra");
            fsp.getSourcePath().add(tempDir);
            final var r = fsp.get(testFileName);
            for (int i = 0; i < testStringToFile.length(); i++) {
                final Integer chExpected = (int) testStringToFile.charAt(i);
                final Integer chActual = r.get();
                assertEquals(chExpected, chActual, "different characters at position " + i);
            }
        } finally {
            //noinspection ResultOfMethodCallIgnored
            file.delete();
        }
    }

    private File createTemporaryTestFile() throws IOException {
        final var file = new File(tempDir + ps + testFileName);
        try (final var fw = new FileWriter(file)) {
            fw.write(testStringToFile);
        }
        return file;
    }

    @Test
    public void testFSPFileNotFound() {
        final var fsp = new FileSourceProvider();
        fsp.setSourcePath(new BasicSourcePath());
        try {
            fsp.get(testFileName);
            fail("No exception was thrown");
        } catch (final IOException ioex) {
            // this is ok
        }
    }
}

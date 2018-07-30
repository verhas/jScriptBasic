package com.scriptbasic.sourceproviders;

import com.scriptbasic.readers.SourceReader;
import org.junit.Test;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import static org.junit.Assert.*;

public class TestFileSourceProvider {

    private static final String tempDir = System.getProperty("java.io.tmpdir");
    private static final String ps = File.separator;
    private static final String testFileName = "testFileName";
    private static final String testStringToFile = "hallo hallo";

    @Test
    public void testFSP() throws IOException {
        final File file = createTemporaryTestFile();
        try {
            // get the test file
            final FileSourceProvider fsp = new FileSourceProvider();
            fsp.setSourcePath(new BasicSourcePath());
            fsp.getSourcePath().add(tempDir + ps + "abrakadabra");
            fsp.getSourcePath().add(tempDir);
            final SourceReader r = fsp.get(testFileName);
            for (int i = 0; i < testStringToFile.length(); i++) {
                final Integer chExpected = (int) testStringToFile.charAt(i);
                final Integer chActual = r.get();
                assertEquals("different characters at position " + i, chExpected,
                        chActual);
            }
        } finally {
            //noinspection ResultOfMethodCallIgnored
            file.delete();
        }
    }

    private File createTemporaryTestFile() throws IOException {
        final File file = new File(tempDir + ps + testFileName);
        final FileWriter fw = new FileWriter(file);
        fw.write(testStringToFile);
        fw.close();
        return file;
    }

    @Test
    public void testFSPFileNotFound() {
        final FileSourceProvider fsp = new FileSourceProvider();
        fsp.setSourcePath(new BasicSourcePath());
        try {
            fsp.get(testFileName);
            fail("No exception was thrown");
        } catch (final IOException ioex) {
            // this is ok
        }
    }
}

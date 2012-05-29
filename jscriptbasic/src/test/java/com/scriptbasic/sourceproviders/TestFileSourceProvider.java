package com.scriptbasic.sourceproviders;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import com.scriptbasic.interfaces.Reader;

import junit.framework.TestCase;

public class TestFileSourceProvider extends TestCase {
    public TestFileSourceProvider(String testName) {
        super(testName);
    }

    private static final String tempDir = System.getProperty("java.io.tmpdir");
    private static final String ps = System.getProperty("path.separator");
    private static final String testFileName = "testFileName";
    private static final String testStringToFile = "hallo hallo";

    public void testFSP() throws IOException {
        // create the test file
        File file = new File(tempDir + ps + testFileName);
        FileWriter fw = new FileWriter(file);
        fw.write(testStringToFile);
        fw.close();

        // get the test file
        FileSourceProvider fsp = new FileSourceProvider();
        fsp.setSourcePath(new BasicSourcePath());
        fsp.getSourcePath().add(tempDir + ps + "abrakadabra");
        fsp.getSourcePath().add(tempDir);
        Reader r = fsp.get(testFileName);
        for (int i = 0; i < testStringToFile.length(); i++) {
            Integer chExpected = (Integer) (int) testStringToFile.charAt(i);
            Integer chActual = r.get();
            assertEquals("different characters at position " + i, chExpected,
                    chActual);
        }
        file.delete();
    }

    public void testFSPFileNotFound() {
        FileSourceProvider fsp = new FileSourceProvider();
        fsp.setSourcePath(new BasicSourcePath());
        try {
            fsp.get(testFileName);
            assertTrue("No exception was thrown", false);
        } catch (IOException ioex) {
            // this is ok
        }
    }
}

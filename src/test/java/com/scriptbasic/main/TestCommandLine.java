package com.scriptbasic.main;

import org.junit.jupiter.api.Test;

import static com.scriptbasic.main.CommandLine.main;

public class TestCommandLine {

    @Test
    public void testProgram() throws Exception {
        main(new String[]{"src/test/resources/com/scriptbasic/testprograms/TestIf1.bas"});
    }

    @Test
    public void testBadProgram() throws Exception {
        main(new String[]{"src/test/resources/com/scriptbasic/testprograms/TestBad.bas"});
    }

    @Test
    public void testNoExtension() {
//		main(new String[] { "1" });
    }

    @Test
    public void testFileHandling() throws Exception {
        System.setProperty("sb4j.extensionclasses", "com.scriptbasic.utility.functions.file.FileHandlingFunctions");
        main(new String[]{"src/test/resources/com/scriptbasic/testprograms/TestFile.bas"});
    }
}

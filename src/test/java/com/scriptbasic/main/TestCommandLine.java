package com.scriptbasic.main;

import org.junit.jupiter.api.Test;

import java.security.Permission;

import static com.scriptbasic.main.CommandLine.main;
import static org.junit.jupiter.api.Assertions.fail;

public class TestCommandLine {

    @Test
    public void testNoArgs() throws Exception {
        final var oldSm = System.getSecurityManager();
        final var sm = new SystemExitIsNotAllowedSecurityManager();
        System.setSecurityManager(sm);
        try {
            main(new String[0]);
            fail();
        } catch (final RuntimeException rte) {
        } finally {
            System.setSecurityManager(oldSm);
        }
    }

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

    public static class SystemExitIsNotAllowedSecurityManager extends SecurityManager {

        @Override
        public void checkPermission(final Permission perm) {
        }

        @Override
        public void checkExit(final int status) {
            throw new SecurityException();
        }
    }
}

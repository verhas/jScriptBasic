package com.scriptbasic.main;

import org.junit.Assert;
import org.junit.Test;

import java.security.Permission;

import static com.scriptbasic.main.CommandLineExtended.main;

public class TestCommandLine {

    public static class SystemExitIsNotAllowedSecurityManager extends SecurityManager {

        @Override
        public void checkPermission(Permission perm) {
        }

        @Override
        public void checkExit(int status) {
            throw new SecurityException();
        }
    }

	@Test
	public void testNoArgs() throws Exception {
		SecurityManager oldSm = System.getSecurityManager();
		SystemExitIsNotAllowedSecurityManager sm = new SystemExitIsNotAllowedSecurityManager();
		System.setSecurityManager(sm);
		try {
			main(new String[0]);
			Assert.fail();
		} catch (RuntimeException rte) {
		} finally {
			System.setSecurityManager(oldSm);
		}
	}

	@Test
	public void testProgram() throws Exception {
		main(new String[] { "src/test/resources/com/scriptbasic/testprograms/TestIf.bas" });
	}

	@Test
	public void testBadProgram() throws Exception {
		main(new String[] { "src/test/resources/com/scriptbasic/testprograms/TestBad.bas" });
	}

	@Test
	public void testNoExtention() throws Exception {
//		main(new String[] { "1" });
	}

	@Test
	public void testFileHandling() throws Exception {
		System.setProperty("sb4j.extensionclasses", "com.scriptbasic.utility.functions.file.FileHandlingFunctions");
		main(new String[] { "src/test/resources/com/scriptbasic/testprograms/TestFile.bas" });
	}
}

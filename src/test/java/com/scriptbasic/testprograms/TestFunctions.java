package com.scriptbasic.testprograms;

import com.scriptbasic.TestingExecutor;
import org.junit.Test;

public class TestFunctions {
    private static void codeTest(final String fileName)
            throws Exception {
        final var e = new TestingExecutor();
        e.setMap(null);
        e.execute(fileName);
    }


    @Test
    public void testMath() throws Exception {
        codeTest("TestMath.bas");
    }
}

/**
 * 
 */
package com.scriptbasic.testprograms;

import junit.framework.TestCase;

import com.scriptbasic.Executor;
import com.scriptbasic.interfaces.AnalysisException;
import com.scriptbasic.interfaces.BasicRuntimeException;

/**
 * @author Peter Verhas
 * @date July 13, 2012
 * 
 */
public class TestPrograms extends TestCase {

    private static void codeTest(String fileName, String expectedOutput)
            throws Exception {
        Executor e = new Executor();
        e.execute(fileName);
        e.assertOutput(expectedOutput);
    }

    private static void testSyntaxFail(String fileName) throws Exception {
        try {
            codeTest(fileName, null);
            assertTrue(false);
        } catch (AnalysisException e) {
            Exception ex = e;
            // OK
        }
    }

    public static void testPrograms() throws Exception {
        codeTest("TestEmpty.bas", "");
        codeTest("TestPrintHello.bas", "hello");
        codeTest("TestIf.bas", "111");
        codeTest("TestBooleanConversions.bas", "111111");
        codeTest("TestArrays.bas", "OK");
        try {
            codeTest("TestNegativeIndex.bas", null);
            assertFalse(true);
        } catch (BasicRuntimeException e) {
        }
        try {
            codeTest("TestNonObjectFieldAccess.bas", null);
            assertFalse(true);
        } catch (BasicRuntimeException e) {
        }
        codeTest("TestSub1.bas", "6");
        codeTest("TestSub2.bas", "21");
        codeTest("TestSub3.bas", "21undef");
        codeTest("TestSub4.bas", "123\n123\n123\n123\n");
        codeTest("TestAbs.bas", "31255");
        codeTest("TestSub5.bas", "1111");

        testSyntaxFail("NestedSub.bas");
        testSyntaxFail("DisplacedGlobal.bas");
        testSyntaxFail("DisplacedLocal.bas");
        testSyntaxFail("LocalUse.bas");
        testSyntaxFail("LocalMethod.bas");
        testSyntaxFail("GlobalLocal.bas");
        codeTest("TestForLoop.bas", "");
    }
}

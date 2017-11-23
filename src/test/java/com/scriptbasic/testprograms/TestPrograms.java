/**
 *
 */
package com.scriptbasic.testprograms;

import com.scriptbasic.Executor;
import com.scriptbasic.interfaces.AnalysisException;
import com.scriptbasic.interfaces.BasicRuntimeException;
import com.scriptbasic.interfaces.ExecutionException;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * @author Peter Verhas date July 13, 2012
 */
public class TestPrograms {

    private static void codeTest(String fileName, Map<String, Object> map,
                                 String expectedOutput) throws Exception {
        Executor e = new Executor();
        e.setMap(map);
        e.execute(fileName);
        e.assertOutput(expectedOutput);
    }

    private static void codeTest(String fileName, String expectedOutput)
            throws Exception {
        codeTest(fileName, null, expectedOutput);
    }

    private static void testSyntaxFail(String fileName) throws Exception {
        try {
            codeTest(fileName, null);
            assertTrue(false);
        } catch (AnalysisException e) {
            @SuppressWarnings("unused")
            Exception ex = e;
            // OK
        }
    }

    private static void testRuntimeFail(String fileName) throws Exception {
        try {
            codeTest(fileName, null);
            assertTrue(false);
        } catch (ExecutionException e) {
            @SuppressWarnings("unused")
            Exception ex = e;
            // OK
        }
    }

    @Test
    public void testPrograms() throws Exception {
        Map<String, Object> map;
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
        codeTest("TestSub5.bas", "1111");
        codeTest("TestWhile1.bas", "89");

        testSyntaxFail("ErrorTestSub1.bas");
        testSyntaxFail("NestedSub.bas");
        testSyntaxFail("DisplacedGlobal.bas");
        testSyntaxFail("DisplacedLocal.bas");
        testSyntaxFail("LocalUse.bas");
        testSyntaxFail("LocalMethod.bas");
        testSyntaxFail("GlobalLocal.bas");
        testSyntaxFail("ErrorTestUse1.bas");
        testSyntaxFail("ErrorTestUse2.bas");
        testSyntaxFail("ErrorNext.bas");
        testSyntaxFail("ErrorTotalCrap.bas");
        testSyntaxFail("ErrorCallClosingParenthesisMissing.bas");

        codeTest("SimpleCall.bas", "");
        codeTest("TestForLoop.bas", "");
        codeTest("TestForLoop1.bas", "123456789");
        codeTest("TestForLoop2.bas", "123456789");
        codeTest("TestForLoop3.bas",
                "1.01.52.02.53.03.54.04.55.05.56.06.57.07.58.08.59.09.5");
        codeTest("TestForLoop4.bas", "987654321");
        codeTest("TestForLoop5.bas",
                "9.08.58.07.57.06.56.05.55.04.54.03.53.02.52.01.51.0");
        codeTest("TestForLoop6.bas", "");
        codeTest("TestForLoop7.bas", "");
        codeTest("TestForLoop8.bas", "22");
        codeTest("TestForLoop9.bas", "111213212223313233");
        codeTest("TestForLoop10.bas", "12323");
        codeTest("TestRuntimeFunction.bas", "1.01.5707963267948966");
        codeTest("TestNullFunction.bas", "undefundef");
        codeTest("TestMethodCall.bas",
                "" + Math.sin(1.0) + "\n" + Math.sin(1.0));
        map = new HashMap<String, Object>();
        map.put("testClass", new TestClass());
        codeTest("TestObjectMethodCall.bas", map, "310");

        testRuntimeFail("TestBadArray1.bas");
        codeTest("TestBadArray2.bas", "");
        codeTest("TestBadArray3.bas", "");
        codeTest("BubbleSort.bas", "-1\n0\n2\n3\n7\n58\n99\n");
        codeTest("TestStringFormat.bas", "314");
        codeTest("TestAbs.bas", "131355.377.7");
        codeTest("TestChomp.bas", "ttt");
        codeTest("TestConvert.bas", "0.9074467814501962");
        codeTest("TestFile.bas", "");
    }

    @Test
    public void testLength() throws Exception {
        codeTest("TestLengthFunction.bas", "");
    }

    /**
     * This is not really a unit test because it does not check that the actual
     * BASIC program sends the strings to the standard log output.
     * It would be too complex and mainly pointless, since the functionality is
     * simple and redirecting the log to a channel that can be checked during test
     * execution is more complex than the whole functionality.
     * <p>
     * Just look at the log and search for the messages
     * "this is an error message"
     * "this is an info message"
     * "this is a debug message"
     * <p>
     * Note that some may be missing in case the logging is configured not to emit debug messages
     *
     * @throws Exception
     */
    @Test
    public void testLogging() throws Exception {
        codeTest("TestLogging.bas", "");
    }

    @Test
    public void testRecordUse() throws Exception {
        codeTest("TestRecordUse.bas", "3haho5555sss");
    }
    @Test
    public void testNew() throws Exception {
        codeTest("TestNew.bas", "6");
    }
    public static class TestClass {
        public int callMe() {
            return 3;
        }

        public long callMe(Long s) {
            return 2 * s;
        }
    }
}

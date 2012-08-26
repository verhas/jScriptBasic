/**
 * 
 */
package com.scriptbasic.testprograms;

import java.util.HashMap;
import java.util.Map;

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

    public static class TestClass {
        public int callMe() {
            return 3;
        }

        public long callMe(Long s) {
            return 2 * s;
        }
    }

    public static void testPrograms() throws Exception {
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
        codeTest("TestRuntimeFunction.bas", "1.01.5707963267948966");
        codeTest("TestNullFunction.bas", "undefundef");
        codeTest("TestMethodCall.bas",
                "" + Math.sin(1.0) + "\n" + Math.sin(1.0));
        map = new HashMap<String, Object>();
        map.put("testClass", new TestClass());
        codeTest("TestObjectMethodCall.bas", map, "310");
    }
}

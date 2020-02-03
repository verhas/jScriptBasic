package com.scriptbasic.testprograms;

import com.scriptbasic.TestingExecutor;
import com.scriptbasic.api.ScriptBasicException;
import com.scriptbasic.configuration.BasicConfiguration;
import com.scriptbasic.exceptions.CommandFactoryException;
import com.scriptbasic.interfaces.AnalysisException;
import com.scriptbasic.interfaces.BasicRuntimeException;
import com.scriptbasic.interfaces.BasicSyntaxException;
import com.scriptbasic.spi.BasicArray;
import com.scriptbasic.spi.BasicValue;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.fail;

/**
 * @author Peter Verhas date July 13, 2012
 */
public class TestPrograms {

    private static void codeTest(final String fileName, final Map<String, Object> map,
                                 final String expectedOutput) throws Exception {
        final var e = new TestingExecutor();
        e.setMap(map);
        e.execute(fileName);
        e.assertOutput(expectedOutput);
    }

    private static void codeTest(final String fileName, final String expectedOutput)
        throws Exception {
        codeTest(fileName, null, expectedOutput);
    }

    private static void testSyntaxFail(final String fileName) throws Exception {
        try {
            codeTest(fileName, null);
            fail();
        } catch (final AnalysisException e) {
            @SuppressWarnings("unused") final Exception ex = e;
            // OK
        }
    }

    private static void testRuntimeFail(final String fileName) throws Exception {
        try {
            codeTest(fileName, null);
            fail();
        } catch (final ScriptBasicException e) {
            @SuppressWarnings("unused") final Exception ex = e;
            // OK
        }
    }

    @Test
    public void testPrograms() throws Exception {
        final Map<String, Object> map;
        codeTest("TestEmpty.bas", "");
        codeTest("TestPrintHello.bas", "hello");
        codeTest("TestIf1.bas", "111");
        codeTest("TestIf2.bas", "12568");
        codeTest("TestIf3.bas", "12456");
        codeTest("TestIf4.bas", "135910");
        codeTest("TestIf5.bas", "1");
        testSyntaxFail("TestIncorrectIf1.bas");
        testSyntaxFail("TestIncorrectIf2.bas");
        testSyntaxFail("TestIncorrectIf3.bas");
        testSyntaxFail("TestIncorrectIf4.bas");
        codeTest("TestSelect1.bas", "1111111");
        codeTest("TestSelect2.bas", "2468");
        codeTest("TestSelect3.bas", "0 1 1 2 3 5 8");
        testSyntaxFail("TestSelectBadSyntax1.bas");
        testSyntaxFail("TestSelectBadSyntax2.bas");
        testSyntaxFail("TestSelectBadSyntax3.bas");
        testSyntaxFail("TestSelectBadSyntax4.bas");
        codeTest("TestSingleLine.bas", "12345678910");
        codeTest("TestBooleanConversions.bas", "111111");
        codeTest("TestArrays.bas", "OK");
        try {
            codeTest("TestNegativeIndex.bas", null);
            fail();
        } catch (final BasicRuntimeException e) {
        }
        try {
            codeTest("TestNonObjectFieldAccess.bas", null);
            fail();
        } catch (final BasicRuntimeException e) {
        }
        codeTest("TestSub1.bas", "6");
        codeTest("TestSub2.bas", "21");
        codeTest("TestSub3.bas", "21");
        codeTest("TestSub4.bas", "123\n123\n123\n123\n");
        codeTest("TestSub5.bas", "1111");
        codeTest("TestWhile1.bas", "89");
        codeTest("MethodCaseInsensitive.bas", "variable g: 0.8");

        testSyntaxFail("ErrorTestSub1.bas");
        testRuntimeFail("ClassMethodsAreCaseSensitive.bas");
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
        codeTest("TestNullFunction.bas", "undef");
        codeTest("TestMethodCall.bas",
            "" + Math.sin(1.0) + "\n" + Math.sin(1.0));
        map = new HashMap<>();
        map.put("testClass", new TestClass());
        codeTest("TestObjectMethodCall.bas", map, "310");

        testRuntimeFail("TestBadArray1.bas");
        codeTest("TestBadArray2.bas", "");
        testRuntimeFail("TestBadArray3.bas");
        codeTest("BubbleSort.bas", "-1\n0\n2\n3\n7\n58\n99\n");
        codeTest("TestAbs.bas", "131355.377.7");
        codeTest("TestChomp.bas", "ttt");
        codeTest("TestConvert.bas", "0.9074467814501962");
        codeTest("TestFile.bas", "");
    }

    @Test
    public void canDefineAndUseDslSentences() throws Exception {
        codeTest("TestDslLines.bas", "79OK");
    }

    @Test
    public void canCompareUndefValues() throws Exception {
        codeTest("TestUndefCompare.bas", "OK");
    }

    @Test
    public void testStringFormat() throws Exception {
        codeTest("TestStringFormat.bas", "314");
    }

    @Test
    public void testStringConcatenation() throws Exception {
        codeTest("TestStringConcatenation.bas", "");
    }

    @Test
    public void testStringFunctions() throws Exception {
        codeTest("TestStringFunctions.bas", "0189123");
    }

    @Test
    public void testUtilityFunctions() throws Exception {
        codeTest("TestUtilityFunctions.bas", "DONE");
    }

    @Test
    public void testJavaObjectFieldAccess() throws ScriptBasicException, ClassNotFoundException, AnalysisException {
        final var e = new TestingExecutor();
        final var t = new TestAccessFields();
        final Map<String, Object> variables = Map.of("T", t);
        e.setMap(variables);
        e.execute("JavaObjectFieldAccess.bas");
        Assertions.assertEquals("13", t.z);
        Assertions.assertEquals("wuff", ((BasicValue) t.h.get(0)).getValue());
        Assertions.assertEquals("mopp", ((BasicValue) t.h.get(1)).getValue());
        Assertions.assertEquals(17L, ((BasicValue) t.h.get(2)).getValue());
        e.assertOutput("");
    }

    @Test()
    public void tooLargeArrayCannotBeCreated() {
        final var e = new TestingExecutor();
        final var configuration = new BasicConfiguration();
        e.getCtx().configuration = configuration;
        configuration.set("arrayMaxIndex", "100");
        Assertions.assertThrows(BasicRuntimeException.class, () ->
            e.execute("AllocateTooLargeArray.bas"));
    }

    @Test()
    public void syntaxErrorWhenThereIsNoClosingParentheseAfterFunctionCall() {
        Assertions.assertThrows(CommandFactoryException.class, () ->
            codeTest("NoClosingParenAfterFunctionCall.bas", ""));
    }

    @Test()
    public void syntaxErrorWhenNoClosingParenInExpression() {
        Assertions.assertThrows(CommandFactoryException.class, () ->
            codeTest("NoClosingParenInExpression.bas", ""));
    }

    @Test()
    public void syntaxErrorWhenNoClosingBracketAccessingArrayElement() {
        Assertions.assertThrows(CommandFactoryException.class, () ->
            codeTest("NoClosingBracketAccessingArrayElement.bas", ""));
    }

    @Test()
    public void syntaxErrorWhenSubroutineIsDefinedMoreThanOnce() {
        Assertions.assertThrows(BasicSyntaxException.class, () ->
            codeTest("SubroutineDoubleDefined.bas", ""));
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
     * @throws Exception in case of exception
     */
    @Test
    public void testLogging() throws Exception {
        codeTest("TestLogging.bas", "");
    }

    @Test
    public void testRecordUse() throws Exception {
        codeTest("TestRecordUse.bas", "3haho5555sss");
    }

    public static class TestAccessFields {
        public final BasicArray h = BasicArray.create(new Object[0]);
        public String z;

        public TestAccessFields() throws ScriptBasicException {
        }
    }

    public static class TestClass {
        public int callMe() {
            return 3;
        }

        public long callMe(final Long s) {
            return 2 * s;
        }
    }
}

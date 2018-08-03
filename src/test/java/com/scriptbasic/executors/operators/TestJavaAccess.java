package com.scriptbasic.executors.operators;

import com.scriptbasic.api.ScriptBasicException;
import com.scriptbasic.interfaces.AnalysisException;
import com.scriptbasic.log.Logger;
import com.scriptbasic.log.LoggerFactory;
import org.junit.Test;

import java.util.Arrays;
import java.util.stream.Collectors;

import static com.scriptbasic.executors.operators.SupportTest.assertValueOfVariable_A;
import static com.scriptbasic.executors.operators.SupportTest.eval;

/**
 * @author Peter Verhas
 * date June 28, 2012
 */

public class TestJavaAccess {
    private static final Logger log = LoggerFactory.getLogger();


    private static void executeInsecure(final String s, final Object expected) throws AnalysisException, ScriptBasicException {
        final var interpreter = eval(s);
        interpreter.getConfiguration().set("insecure", "true");
        interpreter.execute();
        assertValueOfVariable_A(interpreter, expected);
    }

    private static void executeSecure(final String s, final Object expected) throws AnalysisException, ScriptBasicException {
        final var interpreter = eval(s);
        interpreter.execute();
        assertValueOfVariable_A(interpreter, expected);
    }

    private static String program(final String... lines) {
        final var code = String.join("\n", lines);
        log.debug(code);
        return code;
    }

    @Test
    public void canUseOverloadedMethodInsecureMode() throws Exception {
        executeInsecure(program(
                "",
                "rem this is a command line",
                "' this is another command line",
                "use OverloadedMethods from com.scriptbasic.executors.operators.TestJavaAccess as q",
                "method A from com.scriptbasic.executors.operators.TestJavaAccess.OverloadedMethods is (int) use as aint",
                "a=q.aint(1)"), 1);
    }

    @Test(expected = ScriptBasicException.class)
    public void cannotUseOverloadedMethodSecureMode() throws Exception {
        executeSecure(program(
                "use OverloadedMethods from com.scriptbasic.executors.operators.TestJavaAccess as q"
        ), null);
    }

    @Test(expected = ScriptBasicException.class)
    public void cannotMethodOverloadedMethodSecureMode() throws Exception {
        executeSecure(program(
                "method A from com.scriptbasic.executors.operators.TestJavaAccess.OverloadedMethods is (int) use as aint"
        ), null);
    }

    @Test
    public void canUseMathSin() throws Exception {
        executeInsecure(program("use Math from java.lang as m",
                "method sin from java.lang.Math is (double) use as sinus",
                "a=m.sinus(1.0)"), Math.sin(1.0));
    }

    @Test
    public void canUseMathSinDeclaredWithStrings() throws Exception {
        executeInsecure(program(
                "use \"Math\" from \"java\".lang as \"m\"",
                "method sin from \"java.lang.Math\" is (\"double\") use as sinus",
                "a=m.sinus(1.0)"), Math.sin(1.0));
    }

    @Test
    public void canUseMathSinWithoutAlias() throws Exception {
        executeInsecure(program("\n", "use Math from java.lang as m",
                "method sin from java.lang.Math is (double)", "a=m.sin(1.0)"),
                Math.sin(1.0));

    }

    @Test
    public void canUseDoubleValueOf() throws Exception {
        executeInsecure(program("use Double from java.lang", "a=Double.valueOf(\"1.0\")"), 1.0);
    }

    public static class OverloadedMethods {
        public static int A(final long z) {
            log.debug("A(Long) was invoked with value {}", z);
            return 1;
        }

        public static int A(final String z) {
            log.debug("A(String) was invoked with value {}", z);
            return 1;
        }

        public static int A(final int z) {
            log.debug("A(int) was invoked with value {}", z);
            return 1;
        }
    }
}

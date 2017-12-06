package com.scriptbasic.executors.operators;

import com.scriptbasic.interfaces.AnalysisException;
import com.scriptbasic.interfaces.ExecutionException;
import com.scriptbasic.interfaces.Interpreter;
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
    private static Logger log = LoggerFactory.getLogger();


    private static void b(final String s, Object expected) throws AnalysisException, ExecutionException {
        Interpreter interpreter = eval(s);
        interpreter.execute();
        assertValueOfVariable_A(interpreter, expected);
    }

    private static String program(String... lines) {
        final String code = Arrays.stream(lines).collect(Collectors.joining("\n"));
        log.debug(code);
        return code;
    }

    @Test
    public void test1() throws Exception {
        b(program(
                "",
                "rem this is a command line",
                "' this is another command line",
                "use OverloadedMethods from com.scriptbasic.executors.operators.TestJavaAccess as q",
                "method A from com.scriptbasic.executors.operators.TestJavaAccess.OverloadedMethods is (int) use as aint",
                "a=q.aint(1)"), 1);
        /*
         * use "Math" from java.lang as m method "java.lang.Math.sin" is
         * ("double") method "java.lang.Math.wait" is ("long","int")
         */
        b(program("use Math from java.lang as m",
                "method sin from java.lang.Math is (double) use as sinus",
                "a=m.sinus(1.0)"), Math.sin(1.0));
        b(program(
                "use \"Math\" from \"java\".lang as \"m\"",
                "method sin from \"java.lang.Math\" is (\"double\") use as sinus",
                "a=m.sinus(1.0)"), Math.sin(1.0));
        b(program("\n", "use Math from java.lang as m",
                "method sin from java.lang.Math is (double)", "a=m.sin(1.0)"),
                Math.sin(1.0));

        b(program("use Double from java.lang", "a=Double.valueOf(\"1.0\")"), 1.0);

    }

    public static class OverloadedMethods {
        public static int A(long z) {
            log.debug("A(Long) was invoked with value {}", z);
            return 1;
        }

        public static int A(String z) {
            log.debug("A(String) was invoked with value {}", z);
            return 1;
        }

        public static int A(int z) {
            log.debug("A(int) was invoked with value {}", z);
            return 1;
        }
    }
}

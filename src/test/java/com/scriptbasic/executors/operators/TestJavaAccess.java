package com.scriptbasic.executors.operators;

import com.scriptbasic.factories.BasicFactory;
import com.scriptbasic.interfaces.AnalysisException;
import com.scriptbasic.interfaces.ExecutionException;
import com.scriptbasic.interfaces.ExtendedInterpreter;
import com.scriptbasic.interfaces.Factory;
import com.scriptbasic.log.Logger;
import com.scriptbasic.log.LoggerFactory;
import com.scriptbasic.utility.FactoryUtility;
import org.junit.Test;

import static com.scriptbasic.lexer.LexTestHelper.createStringReading;
import static org.junit.Assert.assertEquals;

/**
 * @author Peter Verhas
 * date June 28, 2012
 */

public class TestJavaAccess {
    private static Logger log = LoggerFactory.getLogger();
    private static Factory factory = new BasicFactory();

    private static ExtendedInterpreter ana(final String s)
            throws AnalysisException {
        factory.clean();
        createStringReading(factory, s);
        ExtendedInterpreter eInterpreter = FactoryUtility
                .getExtendedInterpreter(factory);
        eInterpreter.setProgram(FactoryUtility.getSyntaxAnalyzer(factory)
                .analyze());
        return eInterpreter;
    }

    private static void asserta(ExtendedInterpreter interpreter, Object expected)
            throws ExecutionException {
        Object actual = interpreter.getVariable("a");
        if (expected instanceof Integer) {
            expected = ((Integer) expected).longValue();
        }
        if (expected instanceof Double) {
            assertEquals((Double) expected, (Double) actual, 0.000001);
        } else {
            assertEquals(expected, actual);
        }
    }

    private static void b(final String s, Object expected)
            throws AnalysisException, ExecutionException {
        ExtendedInterpreter interpreter = ana(s);
        interpreter.execute();
        asserta(interpreter, expected);
    }

    private static String program(String... lines) {
        int t = 0;
        for (String line : lines) {
            t += line.length() + 1;
        }
        StringBuilder sb = new StringBuilder(t);
        for (String line : lines) {
            sb.append(line);
            sb.append("\n");
        }
        log.debug(sb.toString());
        return sb.toString();
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

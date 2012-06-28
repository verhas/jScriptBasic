/**
 * 
 */
package com.scriptbasic.executors.operators;

import static com.scriptbasic.lexer.LexTestHelper.createStringReading;
import junit.framework.TestCase;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.scriptbasic.factories.BasicFactory;
import com.scriptbasic.interfaces.AnalysisException;
import com.scriptbasic.interfaces.ExecutionException;
import com.scriptbasic.interfaces.ExtendedInterpreter;
import com.scriptbasic.interfaces.Factory;
import com.scriptbasic.utility.FactoryUtilities;

/**
 * @author Peter Verhas
 * @date June 28, 2012
 * 
 */
public class TestJavaAccess extends TestCase {
    private static Logger log = LoggerFactory.getLogger(TestJavaAccess.class);
    private static Factory factory = new BasicFactory();

    private static ExtendedInterpreter ana(final String s)
            throws AnalysisException {
        factory.clean();
        createStringReading(factory, s);
        ExtendedInterpreter eInterpreter = FactoryUtilities
                .getExtendedInterpreter(factory);
        eInterpreter.setProgram(FactoryUtilities.getSyntaxAnalyzer(factory)
                .analyze());
        return eInterpreter;
    }

    private static void asserta(ExtendedInterpreter eInterpreter,
            Object expected) throws ExecutionException {
        Object actual = eInterpreter.getVariable("a");
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
        ExtendedInterpreter eInterpreter = ana(s);
        eInterpreter.execute();
        asserta(eInterpreter, expected);
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

    public static void test1() throws Exception {

        /*
         * use "Math" from java.lang as m method "java.lang.Math.sin" is
         * ("double") method "java.lang.Math.wait" is ("long","int")
         */
        b(program("use Math from java.lang as m",
                "method sin from java.lang.Math is (double) use as sinus",
                "a=m.sinus(1.0)"), Math.sin(1.0));
        b(program("use \"Math\" from \"java\".lang as \"m\"",
                "method sin from \"java.lang.Math\" is (\"double\") use as sinus",
                "a=m.sinus(1.0)"), Math.sin(1.0));
        b(program("use Math from java.lang as m",
                "method sin from java.lang.Math is (double)",
                "a=m.sin(1.0)"), Math.sin(1.0));
    }
}

/**
 * 
 */
package com.scriptbasic.executors.operators;

import static com.scriptbasic.lexer.LexTestHelper.createStringReading;
import junit.framework.TestCase;

import com.scriptbasic.factories.BasicFactory;
import com.scriptbasic.interfaces.AnalysisException;
import com.scriptbasic.interfaces.ExecutionException;
import com.scriptbasic.interfaces.ExtendedInterpreter;
import com.scriptbasic.interfaces.Factory;
import com.scriptbasic.utility.FactoryUtilities;
import com.sun.org.apache.xpath.internal.operations.Plus;

/**
 * @author Peter Verhas
 * @date June 23, 2012
 * 
 */
public class TestOperators extends TestCase {
    private static Factory factory = new BasicFactory();

    private static void c(final String s, final Object expected)
            throws AnalysisException, ExecutionException {
        factory.clean();
        createStringReading(factory, s);

        ExtendedInterpreter eInterpreter = FactoryUtilities
                .getExtendedInterpreter(factory);
        eInterpreter.setProgram(FactoryUtilities.getSyntaxAnalyzer(factory)
                .analyze());
        eInterpreter.execute();
        Object actual = eInterpreter.getVariable("a");
        if (expected instanceof Double) {
            assertEquals((Double) expected, (Double) actual, 0.000001);
        } else {
            assertEquals(expected, actual);
        }
    }

    public static void testOperators() throws AnalysisException,
            ExecutionException {
        c("a=1^3", (Double) 1.0);
        c("a=1*2", (Long) 2L);
        c("a=1.0*2", (Double) 2.0);
        c("a=1.0-2", (Double) (-1.0));
        c("a=2/2", (Long) 1L);
        c("a= 3/2", (Double) 3.0 / 2.0);
        c("a= 3.2/2.0", (Double) 3.2 / 2.0);
        c("a= 3.2/2", (Double) 3.2 / 2.0);
        c("a=3-2", (Long) 1L);
        c("a=3+2", (Long) 5L);
        c("a=3%2", (Long) 1L);
        c("a=+3-3", (Long) 0L);
        c("a=+3.2+-2", (Double) 1.2);
        c("a=-3.4", (Double) (-3.4));
        c("a= not a", true);
        c("a= 3 < 2", false);
        c("a= 3 > 2", true);
        c("a= 3 <= 2", false);
        c("a= 3 >= 2", true);
        c("a= 3 <> 2", true);
        c("a= 3 = 2", false);
        c("a= 3 <> 3", false);
        c("a= 3 = 3", true);

        c("a= \"x\" =  \"x\"", true);
        c("a= \"x\" =  \"y\"", false);
        c("a= false = false", true);
        c("a= true = true", true);
        c("a= false = true", false);
        c("a= true = false", false);

        c("a= 3.0 < 2", false);
        c("a= 3.0 > 2", true);
        c("a= 3.0 <= 2", false);
        c("a= 3.0 >= 2", true);
        c("a= 3.0 <> 2", true);
        c("a= 3.0 = 2", false);
        // c("a= 3.0 <> 3.0", false);
        c("a= 3.0 = 3.0", true);

        c("a= 3.0 < 2.0", false);
        c("a= 3.0 > 2.0", true);
        c("a= 3.0 <= 2.0", false);
        c("a= 3.0 >= 2.0", true);
        c("a= 3.0 <> 2.0", true);
        c("a= 3.0 = 2.0", false);
        // c("a= 3.0 <> 3", false);
        c("a= 3.0 = 3", true);

        c("a = 3 + \"hahhhh\"", "3hahhhh");
        c("a = 3.0 + \"hahhhh\"", "3.0hahhhh");
        c("a = true + \"a\"", "truea");
        c("a= true or false", true);
        c("a = true or (6 / 0)", true);
        c("a= 1 or 2", true);
        c("a=false or false", false);

        c("a=false and true", false);
        c("a=false and false", false);
        c("a=true and false ", false);
        c("a=true and true", true);

        c("a=0 and 1", false);
        c("a=0 and 0", false);
        c("a=1 and 0 ", false);
        c("a=1 and 1", true);

        new PowerOperator().operatorName();
        new MultiplyOperator().operatorName();
        new MinusOperator().operatorName();
        new AddOperator().operatorName();
        new DivideOperator().operatorName();
    }
}

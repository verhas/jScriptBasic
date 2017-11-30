package com.scriptbasic.executors.operators;

import com.scriptbasic.factories.Context;
import com.scriptbasic.factories.ContextBuilder;
import com.scriptbasic.interfaces.AnalysisException;
import com.scriptbasic.interfaces.ExecutionException;
import com.scriptbasic.interfaces.ExtendedInterpreter;

import static org.junit.Assert.assertEquals;

public class SupportTest {

    static ExtendedInterpreter eval(final String s)
            throws AnalysisException {
        Context ctx = ContextBuilder.from(s);
        ctx.interpreter.setProgram(ctx.syntaxAnalyzer.analyze());
        return ctx.interpreter;
    }

    /**
     * Assert that the global variable 'a' managed by the interpreter is as expected.
     *
     * @param interpreter after the execution of the test code holding the global variable 'a'
     * @param expected    the expected value of the variable 'a'
     * @throws ExecutionException
     */
    static void assertValueOfVariable_A(ExtendedInterpreter interpreter, Object expected)
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
}

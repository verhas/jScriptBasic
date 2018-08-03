package com.scriptbasic.executors.operators;

import com.scriptbasic.context.ContextBuilder;
import com.scriptbasic.interfaces.AnalysisException;
import com.scriptbasic.api.ScriptBasicException;
import com.scriptbasic.spi.Interpreter;

import static org.junit.Assert.assertEquals;

class SupportTest {

    static Interpreter eval(final String s)
            throws AnalysisException {
        final var ctx = ContextBuilder.from(s);
        ctx.interpreter.setProgram(ctx.syntaxAnalyzer.analyze());
        return ctx.interpreter;
    }

    /**
     * Assert that the global variable 'a' managed by the interpreter is as expected.
     *
     * @param interpreter after the execution of the test code holding the global variable 'a'
     * @param expected    the expected value of the variable 'a'
     * @throws ScriptBasicException
     */
    static void assertValueOfVariable_A(final Interpreter interpreter, Object expected)
            throws ScriptBasicException {
        final var actual = interpreter.getVariable("a");
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

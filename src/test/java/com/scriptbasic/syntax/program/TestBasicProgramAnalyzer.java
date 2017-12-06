package com.scriptbasic.syntax.program;

import com.scriptbasic.context.Context;
import com.scriptbasic.context.ContextBuilder;
import com.scriptbasic.interfaces.AnalysisException;

import static com.scriptbasic.lexer.LexTestHelper.createStringReading;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;


public class TestBasicProgramAnalyzer {


    private static Context compile(final String s)
            throws AnalysisException {
        Context ctx = ContextBuilder.from(createStringReading(s));
        ctx.interpreter.setProgram(ctx.syntaxAnalyzer.analyze());
        return ctx;
    }

    public void testCorrectProgram() throws Exception {
        compile("a = 4");
        compile("While 13 > 12\nIf 14 > 23 Then\nElse\nEndIf\nWend");
        compile("While 13 > 12\nIf 14 > 23 Then\nEndIf\nWend");
        compile("a=1\nWhile a < 10\na=a+1\nwend");
    }

    public void testOneStepProgramExcute() throws Exception {
        Context ctx = compile("a=1");
        ctx.interpreter.execute();
        Object o = ctx.interpreter.getVariable("a");
        assertTrue(o instanceof Long);
        long l = (Long) o;
        assertEquals(l, 1L);
    }

    public void test2StepsProgramExcute() throws Exception {
        Context ctx = compile("a=1\nb=1+1");
        ctx.interpreter.execute();
        Object o1 = ctx.interpreter.getVariable("a");
        assertTrue(o1 instanceof Long);
        long l1 = (Long) o1;
        assertEquals(l1, 1L);
        Object o2 = ctx.interpreter.getVariable("b");
        assertTrue(o2 instanceof Long);
        long l2 = (Long) o2;
        assertEquals(l2, 2L);
    }

}

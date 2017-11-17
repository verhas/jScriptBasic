package com.scriptbasic.syntax.program;

import com.scriptbasic.factories.BasicFactory;
import com.scriptbasic.interfaces.AnalysisException;
import com.scriptbasic.interfaces.BuildableProgram;
import com.scriptbasic.interfaces.ExtendedInterpreter;
import com.scriptbasic.interfaces.Factory;
import com.scriptbasic.utility.FactoryUtility;

import static com.scriptbasic.lexer.LexTestHelper.createStringReading;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@SuppressWarnings("static-method")
public class TestBasicProgramAnalyzer {

    private static Factory factory = new BasicFactory();

    private static BuildableProgram compile(final String s)
            throws AnalysisException {
        factory.clean();
        createStringReading(factory, s);
        return FactoryUtility.getSyntaxAnalyzer(factory).analyze();
    }

    public void testCorrectProgram() throws Exception {
        compile("a = 4");
        compile("While 13 > 12\nIf 14 > 23 Then\nElse\nEndIf\nWend");
        compile("While 13 > 12\nIf 14 > 23 Then\nEndIf\nWend");
        compile("a=1\nWhile a < 10\na=a+1\nwend");
    }

    public void testOneStepProgramExcute() throws Exception {
        BuildableProgram program = compile("a=1");
        ExtendedInterpreter eInterpreter = FactoryUtility
                .getExtendedInterpreter(factory);
        eInterpreter.setProgram(program);
        eInterpreter.execute();
        Object o = eInterpreter.getVariable("a");
        assertTrue(o instanceof Long);
        long l = (Long) o;
        assertEquals(l, 1L);
    }

    public void test2StepsProgramExcute() throws Exception {
        BuildableProgram program = compile("a=1\nb=1+1");
        ExtendedInterpreter eInterpreter = FactoryUtility
                .getExtendedInterpreter(factory);
        eInterpreter.setProgram(program);
        eInterpreter.execute();
        Object o1 = eInterpreter.getVariable("a");
        assertTrue(o1 instanceof Long);
        long l1 = (Long) o1;
        assertEquals(l1, 1L);
        Object o2 = eInterpreter.getVariable("b");
        assertTrue(o2 instanceof Long);
        long l2 = (Long) o2;
        assertEquals(l2, 2L);
    }

}

package com.scriptbasic.syntax.program;

import static com.scriptbasic.lexer.LexTestHelper.createStringReading;
import junit.framework.TestCase;

import com.scriptbasic.exceptions.AnalysisException;
import com.scriptbasic.factories.BasicFactory;
import com.scriptbasic.interfaces.Factory;
import com.scriptbasic.interfaces.Program;
import com.scriptbasic.utility.FactoryUtilities;

@SuppressWarnings("static-method")
public class TestBasicProgramAnalyzer extends TestCase {

    private static Factory factory = new BasicFactory();

    private static Program compile(final String s) throws AnalysisException {
        factory.clean();
        createStringReading(factory, s);
        return FactoryUtilities.getSyntaxAnalyzer(factory).analyze();
    }

    public void testCorrectProgram() throws Exception {
        compile("While 13 > 12\nIf 14 > 23 Then\nElse\nEndIf\nWend");
        compile("While 13 > 12\nIf 14 > 23 Then\nEndIf\nWend");
    }

}

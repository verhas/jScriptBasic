package com.scriptbasic.syntax.program;

import static com.scriptbasic.lexer.LexTestHelper.createStringReading;
import junit.framework.TestCase;

import com.scriptbasic.factories.BasicFactory;
import com.scriptbasic.interfaces.AnalysisException;
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
        compile("a = 4");
        compile("While 13 > 12\nIf 14 > 23 Then\nElse\nEndIf\nWend");
        compile("While 13 > 12\nIf 14 > 23 Then\nEndIf\nWend");
        compile("a=1\nWhile a < 10\na=a+1\nwend");
    }

}

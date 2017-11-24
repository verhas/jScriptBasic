package com.scriptbasic;

import com.scriptbasic.factories.BasicFactory;
import com.scriptbasic.interfaces.*;
import com.scriptbasic.readers.GenericReader;
import com.scriptbasic.utility.FactoryUtility;
import com.scriptbasic.utility.functions.file.FileHandlingFunctions;
import org.junit.Assert;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.Map;

/**
 * Execute a BASIC program available in the resource file. stdin, stdout and
 * stderr are string builders.
 *
 * @author Peter Verhas date Jul 13, 2012
 */
public class Executor extends AbstractStringIOPojo {

    private Factory factory = new BasicFactory();
    private Map<String, Object> map = null;

    public void setMap(Map<String, Object> map) {
        this.map = map;
    }

    public void execute(String resourceName) throws AnalysisException,
            ExecutionException, ClassNotFoundException {
        factory.clean();
        StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
        InputStream is = Class.forName(stackTrace[2].getClassName())
                .getResourceAsStream(resourceName);
        final java.io.Reader r = new InputStreamReader(is);
        final GenericReader reader = new GenericReader();
        reader.set(r);
        reader.setSourceProvider(null);
        reader.set(resourceName);
        final LexicalAnalyzer lexicalAnalyzer = FactoryUtility
                .getLexicalAnalyzer(factory);
        lexicalAnalyzer.set(reader);
        ExtendedInterpreter interpreter = FactoryUtility
                .getExtendedInterpreter(factory);
        interpreter.registerFunctions(FileHandlingFunctions.class);
        interpreter.setProgram(FactoryUtility.getSyntaxAnalyzer(factory)
                .analyze());
        StringWriter writer = new StringWriter();
        interpreter.setWriter(writer);
        StringWriter errorWriter = new StringWriter();
        interpreter.setErrorWriter(errorWriter);
        StringReader inputReader = getSStdin() == null ? null
                : new StringReader(getSStdin());
        interpreter.setReader(inputReader);
        if (map != null) {
            for (String key : map.keySet()) {
                interpreter.setVariable(key, map.get(key));
            }
        }
        interpreter.execute();
        setSStdout(writer.toString());
    }

    public void assertOutput(String s) {
        Assert.assertEquals(s, getSStdout());
    }

}

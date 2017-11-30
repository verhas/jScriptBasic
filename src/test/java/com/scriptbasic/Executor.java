package com.scriptbasic;

import com.scriptbasic.factories.BasicFactory;
import com.scriptbasic.interfaces.AnalysisException;
import com.scriptbasic.interfaces.ExecutionException;
import com.scriptbasic.interfaces.ExtendedInterpreter;
import com.scriptbasic.interfaces.Factory;
import com.scriptbasic.readers.GenericSourceReader;
import com.scriptbasic.utility.FactoryUtility;
import com.scriptbasic.utility.functions.file.FileHandlingFunctions;
import org.junit.Assert;

import java.io.*;
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
        final Reader r = new InputStreamReader(is);
        final GenericSourceReader reader = new GenericSourceReader(r, null,resourceName);
        ExtendedInterpreter interpreter = FactoryUtility
                .getExtendedInterpreter(factory);
        interpreter.registerFunctions(FileHandlingFunctions.class);
        interpreter.setProgram(FactoryUtility.getSyntaxAnalyzer(factory)
                .analyze());
        StringWriter writer = new StringWriter();
        interpreter.setOutput(writer);
        StringWriter errorWriter = new StringWriter();
        interpreter.setError(errorWriter);
        StringReader inputReader = getSStdin() == null ? null
                : new StringReader(getSStdin());
        interpreter.setInput(inputReader);
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

package com.scriptbasic;

import com.scriptbasic.factories.Context;
import com.scriptbasic.factories.ContextBuilder;
import com.scriptbasic.interfaces.AnalysisException;
import com.scriptbasic.interfaces.ExecutionException;
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

    private Map<String, Object> map = null;

    public void setMap(Map<String, Object> map) {
        this.map = map;
    }

    public void execute(String resourceName) throws AnalysisException,
            ExecutionException, ClassNotFoundException {
        StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
        InputStream is = Class.forName(stackTrace[2].getClassName())
                .getResourceAsStream(resourceName);
        final Reader r = new InputStreamReader(is);
        final Context ctx = ContextBuilder.from(r);
        ctx.interpreter.registerFunctions(FileHandlingFunctions.class);
        ctx.interpreter.setProgram(ctx.syntaxAnalyzer.analyze());
        StringWriter writer = new StringWriter();
        ctx.interpreter.setOutput(writer);
        StringWriter errorWriter = new StringWriter();
        ctx.interpreter.setError(errorWriter);
        StringReader inputReader = getSStdin() == null ? null
                : new StringReader(getSStdin());
        ctx.interpreter.setInput(inputReader);
        if (map != null) {
            for (String key : map.keySet()) {
                ctx.interpreter.setVariable(key, map.get(key));
            }
        }
        ctx.interpreter.execute();
        setSStdout(writer.toString());
    }

    public void assertOutput(String s) {
        Assert.assertEquals(s, getSStdout());
    }

}

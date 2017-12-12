package com.scriptbasic;

import com.scriptbasic.context.Context;
import com.scriptbasic.context.ContextBuilder;
import com.scriptbasic.interfaces.AnalysisException;
import com.scriptbasic.api.ScriptBasicException;
import com.scriptbasic.utility.functions.file.FileHandlingFunctions;
import org.junit.Assert;

import java.io.*;
import java.util.Map;
import java.util.Optional;

/**
 * Execute a BASIC program available in the resource file. stdin, stdout and
 * stderr are string builders.
 *
 * @author Peter Verhas date Jul 13, 2012
 */
public class TestingExecutor extends AbstractStringIOPojo {
    private Context ctx;
    private Map<String, Object> map = null;

    public TestingExecutor(){
      ctx = ContextBuilder.newContext();
    }

    public void setMap(Map<String, Object> map) {
        this.map = map;
    }


    public Context getCtx() {
        return ctx;
    }

    public void execute(String resourceName) throws AnalysisException,
            ScriptBasicException, ClassNotFoundException {
        Optional<StackWalker.StackFrame> frame =
                StackWalker.getInstance(StackWalker.Option.RETAIN_CLASS_REFERENCE).walk(s -> s.skip(1).findFirst());
        if (frame.isPresent()) {

            InputStream is = Class.forName(frame.get().getClassName())
                    .getResourceAsStream(resourceName);
            final Reader r = new InputStreamReader(is);
            ctx = ContextBuilder.from(ctx,r);
            ctx.interpreter.registerFunctions(FileHandlingFunctions.class);
            ctx.interpreter.setProgram(ctx.syntaxAnalyzer.analyze());
            StringWriter writer = new StringWriter();
            ctx.interpreter.setOutput(writer);
            StringWriter errorWriter = new StringWriter();
            ctx.interpreter.setErrorOutput(errorWriter);
            StringReader inputReader = getSStdin() == null ? null
                    : new StringReader(getSStdin());
            ctx.interpreter.setInput(inputReader);
            if (map != null) {
                for (String key : map.keySet()) {
                    ctx.interpreter.setVariable(key, map.get(key));
                }
            }
            ctx.configuration.set("insecure","true");
            ctx.interpreter.execute();
            setSStdout(writer.toString());
        } else {
            throw new ClassNotFoundException();
        }
    }

    public void assertOutput(String s) {
        Assert.assertEquals(s, getSStdout());
    }

}

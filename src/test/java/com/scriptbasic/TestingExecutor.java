package com.scriptbasic;

import com.scriptbasic.api.ScriptBasicException;
import com.scriptbasic.context.Context;
import com.scriptbasic.context.ContextBuilder;
import com.scriptbasic.interfaces.AnalysisException;
import com.scriptbasic.utility.functions.file.FileHandlingFunctions;
import org.junit.jupiter.api.Assertions;

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
public class TestingExecutor extends AbstractStringIOPojo {
    private Context ctx;
    private Map<String, Object> map = null;

    public TestingExecutor() {
        ctx = ContextBuilder.newContext();
    }

    public void setMap(final Map<String, Object> map) {
        this.map = map;
    }


    public Context getCtx() {
        return ctx;
    }

    public void execute(final String resourceName)
            throws AnalysisException,
            ScriptBasicException,
            ClassNotFoundException {
        final var frame = StackWalker.getInstance(StackWalker.Option.RETAIN_CLASS_REFERENCE)
                .walk(s -> s.skip(1).findFirst());
        if (frame.isPresent()) {
            final var is = Class.forName(frame.get().getClassName()).getResourceAsStream(resourceName);
            final var r = new InputStreamReader(is);
            final var writer = new StringWriter();
            final var errorWriter = new StringWriter();
            ctx = ContextBuilder.from(ctx, r);
            ctx.interpreter.registerFunctions(FileHandlingFunctions.class);
            ctx.interpreter.setProgram(ctx.syntaxAnalyzer.analyze());
            ctx.interpreter.setOutput(writer);
            ctx.interpreter.setErrorOutput(errorWriter);
            final var inputReader = getSStdin() == null ? null : new StringReader(getSStdin());
            ctx.interpreter.setInput(inputReader);
            if (map != null) {
                for (final String key : map.keySet()) {
                    ctx.interpreter.setVariable(key, map.get(key));
                }
            }
            ctx.configuration.set("insecure", "true");
            ctx.interpreter.execute();
            setSStdout(writer.toString());
        } else {
            throw new ClassNotFoundException();
        }
    }

    public void assertOutput(final String s) {
        Assertions.assertEquals(s, getSStdout());
    }

}

package com.scriptbasic.executors;

import com.scriptbasic.Engine;
import com.scriptbasic.api.BasicFunction;
import com.scriptbasic.api.ScriptBasic;
import com.scriptbasic.api.ScriptBasicException;
import com.scriptbasic.context.Context;
import com.scriptbasic.spi.Interpreter;
import org.junit.Test;

import java.io.PrintWriter;
import java.lang.reflect.Field;

import static org.junit.Assert.assertEquals;

public class TestInterpreterAwareMethodCall {


    static ScriptBasic sb;

    @BasicFunction(alias = "callMe", classification = Test.class)
    public static void getFirstArgument(Interpreter interpreter, int one, long two) throws NoSuchFieldException, IllegalAccessException {
        assertEquals(1, one);
        assertEquals(2, two);
        Field ctxF = Engine.class.getDeclaredField("ctx");
        ctxF.setAccessible(true);
        Context ctx = (Context) ctxF.get(sb);
        assertEquals(ctx.interpreter, interpreter);
    }

    public static void myPrint(Interpreter interpreter, String s) throws ScriptBasicException {
        ((PrintWriter) interpreter.getOutput()).println(s);
    }

    @Test
    public void passesInterpreterAsFirstArgument() throws ScriptBasicException {
        sb = ScriptBasic.engine();
        sb.registerExtension(this.getClass())
                .function("myPrint").klass(TestInterpreterAwareMethodCall.class).arguments(String.class)
                .output(new PrintWriter(System.out))
                .eval("callMe 1,2\nmyPrint \"TEXT\"");
    }
}
